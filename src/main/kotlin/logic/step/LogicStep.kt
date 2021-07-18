package logic.step

import common.Builder
import telegram.output.OutputEntity
import telegram.input.Update

typealias LogicStepKey = String

interface LogicStep<State> {
    val key: LogicStepKey
    val validator: LogicValidator<State>
    val process: LogicStepResultBuilder<State>.(update: Update, state: State) -> Unit
}

data class LogicStepResult<State> (
    val nextStep: LogicStepKey?,
    val nextState: State,
    val output: List<OutputEntity>,
)

class LogicStepResultBuilder<State>(update: Update, state: State): Builder<LogicStepResult<State>> {
    private var nextStep: LogicStepKey? = null
    var nextState: State = state
    private var output: MutableList<OutputEntity> = mutableListOf()

    fun nextStep(step: LogicStep<State>) {
        nextStep = step.key
    }
    fun nextStepKey(stepKey: LogicStepKey) {
        nextStep = stepKey
    }

    fun addOutput(entity: OutputEntity) {
        output.add(entity)
    }

    override fun build(): LogicStepResult<State> = LogicStepResult(nextStep, nextState, output.toList())
}

