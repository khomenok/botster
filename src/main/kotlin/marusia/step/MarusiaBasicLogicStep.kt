package marusia.step

import common.Builder
import logic.step.*
import marusia.Input
import marusia.MarusiaLogicStep
import marusia.MarusiaLogicStepResultBuilder
import marusia.MarusiaLogicValidator
import java.util.*
import kotlin.properties.Delegates

class MarusiaBasicLogicStep<State>(
    override val key: LogicStepKey,
    override val validator: MarusiaLogicValidator<State>,
    override val process: MarusiaLogicStepResultBuilder<State>.(update: Input, state: State) -> Unit,
) : MarusiaLogicStep<State>

class MarusiaBasicLogicStepBuilder<State>: Builder<MarusiaLogicStep<State>> {
    var key: LogicStepKey? = null
    var validate: MarusiaLogicValidator<State>? = null
    var process: MarusiaLogicStepResultBuilder<State>.(update: Input, state: State) -> Unit by Delegates.notNull()

    override fun build(): MarusiaLogicStep<State> {
        return MarusiaBasicLogicStep(
            key ?: UUID.randomUUID().toString(),
            validate ?: DummyValidator(),
            process,
        )
    }
}