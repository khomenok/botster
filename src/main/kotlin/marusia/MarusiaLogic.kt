package marusia

import common.Builder
import common.builderFun
import logic.Logic
import logic.step.*
import marusia.step.MarusiaBasicLogicStepBuilder
import kotlin.properties.Delegates

typealias MarusiaLogic<State> = Logic<State, Input, Output>
typealias MarusiaLogicStep<State> = LogicStep<State, Input, Output>
typealias MarusiaLogicStepResultBuilder<State> = LogicStepResultBuilder<State, Input, Output>
typealias MarusiaLogicValidator<State> = LogicValidator<State, Input>


// todo: move out of the api, cause this thing is about logic-api
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

// todo: move out of the api, cause this thing is about logic-api
fun <State>logic(init: MarusiaLogicBuilder<State>.() -> Unit): MarusiaLogic<State> = builderFun(MarusiaLogicBuilder(), init)
