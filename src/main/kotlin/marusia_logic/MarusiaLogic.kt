package marusia_logic

import common.Builder
import common.builderFun
import logic.Logic
import logic.steps.*
import marusia.input.MarusiaInput
import marusia.output.MarusiaOutput
import marusia.step.MarusiaBasicLogicStepBuilder
import kotlin.properties.Delegates

typealias MarusiaLogic<State> = Logic<State, MarusiaInput, MarusiaOutput>
typealias MarusiaLogicStep<State> = LogicStep<State, MarusiaInput, MarusiaOutput>
typealias MarusiaLogicStepResultBuilder<State> = LogicStepResultBuilder<State, MarusiaInput, MarusiaOutput>
typealias MarusiaLogicValidator<State> = LogicValidator<State, MarusiaInput>

class MarusiaLogicBuilder<State>(): Builder<MarusiaLogic<State>> {
    private val steps = mutableMapOf<LogicStepKey, MarusiaLogicStep<State>>()
    private val entrypoints = mutableListOf<LogicStepKey>()
    var initialState: () -> State by Delegates.notNull()

    private fun <B : Builder<MarusiaLogicStep<State>>> buildStep(builder: B, init: B.() -> Unit): MarusiaLogicStep<State> {
        val builtStep = builderFun(builder, init)
        // TODO: throw duplicated keys
        steps[builtStep.key] = builtStep
        return builtStep
    }

    fun step(init: MarusiaBasicLogicStepBuilder<State>.() -> Unit)= buildStep(MarusiaBasicLogicStepBuilder(), init)

    fun entrypoint(step: MarusiaLogicStep<State>) {
        entrypoints.add(step.key)
    }

    override fun build(): MarusiaLogic<State> {
        return Logic(steps.toMap(), entrypoints.toList(), initialState)
    }
}

fun <State>marusiaLogic(init: MarusiaLogicBuilder<State>.() -> Unit): MarusiaLogic<State> = builderFun(MarusiaLogicBuilder(), init)
