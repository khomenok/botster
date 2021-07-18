package logic.step

import common.Builder
import telegram.input.Update
import java.util.*
import kotlin.properties.Delegates


class BasicLogicStep<State>(
    override val key: LogicStepKey,
    override val validator: LogicValidator<State>,
    override val process: LogicStepResultBuilder<State>.(update: Update, state: State) -> Unit,
) : LogicStep<State>

class BasicLogicStepBuilder<State>(): Builder<LogicStep<State>> {
    var key: LogicStepKey? = null
    var validate: LogicValidator<State>? = null
    var process: LogicStepResultBuilder<State>.(update: Update, state: State) -> Unit by Delegates.notNull()

    override fun build(): LogicStep<State> {
        return BasicLogicStep(
            key ?: UUID.randomUUID().toString(),
            validate ?: DummyValidator(),
            process,
        )
    }
}