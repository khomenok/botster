package logic.step

import common.Builder
import telegram.input.TextMessage
import telegram.input.Update
import telegram.output.OutputEntity
import java.util.*
import kotlin.properties.Delegates

typealias TelegramLogicStep<State> = LogicStep<State, Update, OutputEntity>
typealias TelegramLogicStepResultBuilder<State> = LogicStepResultBuilder<State, Update, OutputEntity>
typealias TelegramLogicValidator<State> = LogicValidator<State, Update>

class TextMessageValidator<State>() : TelegramLogicValidator<State> {
    override fun validate(update: Update, state: State): Boolean {
        return update.message?.text != null
    }
}

// todo: move out of the logic, cause this thing now about telegram
class TextMessageLogicStepBuilder<State>(): Builder<TelegramLogicStep<State>> {
    var key: LogicStepKey? = null
    var validate: TelegramLogicValidator<State>? = null
    var process: TelegramLogicStepResultBuilder<State>.(update: Update, state: State, message: TextMessage) ->
    Unit by Delegates.notNull()

    override fun build(): TelegramLogicStep<State> {
        val combinedValidator = if (validate == null) TextMessageValidator()
        else CombinedValidator(TextMessageValidator(), validate!!)

        return BasicLogicStep(
            key ?: UUID.randomUUID().toString(),
            combinedValidator,
        ) { update, state -> process(update, state, TextMessage(update.message!!)) }
    }
}