package marusia.step

import common.Builder
import logic.steps.*
import marusia.input.MarusiaInput
import marusia_logic.MarusiaLogicStep
import marusia_logic.MarusiaLogicStepResultBuilder
import marusia_logic.MarusiaLogicValidator
import java.util.*
import kotlin.properties.Delegates

class MarusiaBasicLogicStep<State>(
    override val key: LogicStepKey,
    override val validator: MarusiaLogicValidator<State>,
    override val process: MarusiaLogicStepResultBuilder<State>.(update: MarusiaInput, state: State) -> Unit,
) : MarusiaLogicStep<State>

class MarusiaBasicLogicStepBuilder<State>: Builder<MarusiaLogicStep<State>> {
    var key: LogicStepKey? = null
    var validate: MarusiaLogicValidator<State>? = null
    var process: MarusiaLogicStepResultBuilder<State>.(update: MarusiaInput, state: State) -> Unit by Delegates.notNull()

    override fun build(): MarusiaLogicStep<State> {
        return MarusiaBasicLogicStep(
            key ?: UUID.randomUUID().toString(),
            validate ?: DummyValidator(),
            process,
        )
    }
}