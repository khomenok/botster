package logic.step

import common.Builder
import telegram.input.TextMessage
import telegram.input.Update
import java.util.*
import kotlin.properties.Delegates

class TextMessageValidator<State>() : LogicValidator<State> {
    override fun validate(update: Update, state: State): Boolean {
        return update.message?.text != null
    }
}

class TextMessageLogicStepBuilder<State>(): Builder<LogicStep<State>> {
    var key: LogicStepKey? = null
    var validate: LogicValidator<State>? = null
    var process: LogicStepResultBuilder<State>.(update: Update, state: State, message: TextMessage) ->
    Unit by Delegates.notNull()

    override fun build(): LogicStep<State> {
        val combinedValidator = if (validate == null) TextMessageValidator()
        else CombinedValidator(TextMessageValidator(), validate!!)

        return BasicLogicStep(
            key ?: UUID.randomUUID().toString(),
            combinedValidator,
        ) { update, state -> process(update, state, TextMessage(update.message!!)) }
    }
}