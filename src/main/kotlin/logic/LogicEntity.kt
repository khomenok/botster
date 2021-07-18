package logic

import logic.step.LogicStep
import logic.step.LogicStepKey
import logic.step.LogicStepResult
import logic.step.LogicStepResultBuilder
import telegram.input.Update

class LogicEntity<State>(
    private val steps: Map<LogicStepKey, LogicStep<State>>,
    private val entrypoints: List<LogicStepKey>,
    private var state: State,
) {
    private var currentStep: LogicStepKey? = null

    private fun stepResultBuilderFun(
        builder: LogicStepResultBuilder<State>,
        init: LogicStepResultBuilder<State>.(update: Update, state: State) -> Unit,
        update: Update,
    ): LogicStepResult<State> {
        builder.init(update, state)
        return builder.build()
    }

    private fun processStep(update: Update, step: LogicStep<State>): LogicStepResult<State> {
        val result = stepResultBuilderFun(LogicStepResultBuilder(update, state), step.process, update)
        state = result.nextState
        currentStep = result.nextStep
        return result
    }

    fun process(update: Update): LogicStepResult<State> {
        val allEntrypoints = if (currentStep != null) // TODO: make lists more optimized
            listOf(listOf(currentStep), entrypoints).flatten() else entrypoints

        // TODO: support bad paths
        val entrypoint = allEntrypoints.firstOrNull { steps[it]!!.validator.validate(update, state) }
            ?: return LogicStepResult(currentStep, state, listOf())
        return processStep(update, steps[entrypoint]!!)
    }
}