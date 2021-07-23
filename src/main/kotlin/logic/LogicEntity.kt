package logic

import logic.step.LogicStep
import logic.step.LogicStepKey
import logic.step.LogicStepResult
import logic.step.LogicStepResultBuilder

class LogicEntity<State, Input, Output>(
    private val steps: Map<LogicStepKey, LogicStep<State, Input, Output>>,
    private val entrypoints: List<LogicStepKey>,
    private var state: State,
) {
    private var currentStep: LogicStepKey? = null

    private fun stepResultBuilderFun(
        builder: LogicStepResultBuilder<State, Input, Output>,
        init: LogicStepResultBuilder<State, Input, Output>.(update: Input, state: State) -> Unit,
        update: Input,
    ): LogicStepResult<State, Input, Output> {
        builder.init(update, state)
        return builder.build()
    }

    private fun processStep(
        update: Input,
        step: LogicStep<State, Input, Output>,
    ): LogicStepResult<State, Input, Output> {
        val result = stepResultBuilderFun(LogicStepResultBuilder(update, state), step.process, update)
        state = result.nextState
        currentStep = result.nextStep
        return result
    }

    fun process(update: Input): LogicStepResult<State, Input, Output> {
        val allEntrypoints = if (currentStep != null) // TODO: make lists more optimized
            listOf(listOf(currentStep), entrypoints).flatten() else entrypoints

        // TODO: support bad paths
        val entrypoint = allEntrypoints.firstOrNull { steps[it]!!.validator.validate(update, state) }
            ?: return LogicStepResult(currentStep, state, listOf())
        return processStep(update, steps[entrypoint]!!)
    }
}