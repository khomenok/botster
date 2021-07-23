package logic.step

import common.Builder
import telegram.input.Update
import java.util.*
import kotlin.properties.Delegates


class BasicLogicStep<State, Input, Output>(
    override val key: LogicStepKey,
    override val validator: LogicValidator<State, Input>,
    override val process: LogicStepResultBuilder<State, Input, Output>.(update: Input, state: State) -> Unit,
) : LogicStep<State, Input, Output>

class BasicLogicStepBuilder<State, Input, Output>: Builder<LogicStep<State, Input, Output>> {
    var key: LogicStepKey? = null
    var validate: LogicValidator<State, Input>? = null
    var process: LogicStepResultBuilder<State, Input, Output>.(update: Input, state: State) -> Unit by Delegates.notNull()

    override fun build(): LogicStep<State, Input, Output> {
        return BasicLogicStep(
            key ?: UUID.randomUUID().toString(),
            validate ?: DummyValidator(),
            process,
        )
    }
}