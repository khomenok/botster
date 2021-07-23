package logic.step

import common.Builder
import telegram.output.OutputEntity
import telegram.input.Update

typealias LogicStepKey = String

interface LogicStep<State, Input, Output> {
    val key: LogicStepKey
    val validator: LogicValidator<State, Input>
    val process: LogicStepResultBuilder<State, Input, Output>.(update: Input, state: State) -> Unit
}

data class LogicStepResult<State, Input, Output> (
    val nextStep: LogicStepKey?,
    val nextState: State,
    val output: List<Output>,
)

class LogicStepResultBuilder<State, Input, Output>(
    update: Input,
    state: State,
): Builder<LogicStepResult<State, Input, Output>> {
    private var nextStep: LogicStepKey? = null
    var nextState: State = state
    private var output: MutableList<Output> = mutableListOf()

    fun nextStep(step: LogicStep<State, Input, Output>) {
        nextStep = step.key
    }
    fun nextStepKey(stepKey: LogicStepKey) {
        nextStep = stepKey
    }

    fun addOutput(entity: Output) {
        output.add(entity)
    }

    override fun build(): LogicStepResult<State, Input, Output> = LogicStepResult(nextStep, nextState, output.toList())
}

