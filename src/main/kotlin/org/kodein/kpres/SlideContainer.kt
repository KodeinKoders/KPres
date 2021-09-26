package org.kodein.kpres

import kotlinx.css.*
import react.*
import styled.css
import styled.styledDiv
import kotlinx.browser.window
import org.kodein.kpres.utils.getValue

private fun useTransitionState(getTransitionDuration: (Boolean) -> Int, getTransition: (Boolean) -> Transition): Pair<TransitionState?, (Boolean) -> Unit> {
    var transitionState by useState<TransitionState?>(null)

    useEffect(listOf(transitionState)) {
        val timerId = when (val state = transitionState) {
            is TransitionState.Prepare -> {
                val transitionDuration = getTransitionDuration(state.forward)
                val stateDuration = getTransition(state.forward).stateDuration(transitionDuration, 0).takeIf { it in 0..transitionDuration } ?: transitionDuration
                window.setTimeout({ transitionState = TransitionState.Execute(0, stateDuration, state.forward, transitionDuration - stateDuration) }, 1)
            }
            is TransitionState.Execute -> {
                when (state.remaining) {
                    0 -> window.setTimeout({ transitionState = null }, state.duration)
                    else -> {
                        val stateDuration = getTransition(state.forward).stateDuration(state.remaining, state.state + 1).takeIf { it in 0..state.remaining } ?: state.remaining
                        window.setTimeout({ transitionState = TransitionState.Execute(state.state + 1, stateDuration, state.forward, state.remaining - stateDuration) }, state.duration)
                    }
                }
            }
            null -> null
        }

        if (timerId != null) cleanup { window.clearTimeout(timerId) }
    }

    return transitionState to { forward: Boolean -> transitionState = TransitionState.Prepare(forward) }
}

internal class SlideContainerProps(
    val presProps: PresentationProps,
    val position: SlidePosition,
    val style: CssBuilder.() -> Unit,
    val render: RBuilder.(SlideRender) -> Unit,
) : Props

internal val slideContainer by functionComponent<SlideContainerProps> { props ->
    var currentPosition by useState(props.position)
    var previousPosition by useState(SlidePosition(0, 0))

    val getTransitionDuration = { forward: Boolean -> (if (forward) props.presProps.slideInfos(currentPosition) else props.presProps.slideInfos(previousPosition)).inTransitionDuration ?: props.presProps.defaultTransitionDuration }
    val (appearState, startAppear) = useTransitionState(getTransitionDuration) { props.presProps.transitionSet(currentPosition) { if (it) inTransitions else outTransitions }.appear }
    val (disappearState, startDisappear) = useTransitionState(getTransitionDuration) { props.presProps.transitionSet(previousPosition) { if (it) outTransitions else inTransitions }.disappear }

    useEffect(listOf(props.position.slide, props.position.state)) {
        if (currentPosition.slide != props.position.slide) {
            val forward = props.position.slide > currentPosition.slide
            startAppear(forward)
            startDisappear(forward)
            previousPosition = currentPosition
        }
        currentPosition = props.position
    }

    styledDiv {
        this.key = "container"
        css {
            +"pres-container"
            position = Position.absolute
            top = 0.pct
            left = 0.pct
            width = 100.pct
            height = 100.pct
            overflow = Overflow.hidden
            props.style(this)
        }

        styledDiv {
            css {
                +"inner-container"
                width = 100.pct
                height = 100.pct
                position = Position.relative
            }

            props.render(this, SlideRender(currentPosition, previousPosition, appearState, disappearState))
        }
    }
}
