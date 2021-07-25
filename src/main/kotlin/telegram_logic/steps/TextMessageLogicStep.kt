package telegram_logic.steps

import common.Builder
import logic.steps.BasicLogicStep
import logic.steps.CombinedValidator
import logic.steps.LogicStepKey
import telegram_logic.*
import telegram.input.TextMessage
import telegram.input.TelegramInput
import java.util.*
import kotlin.properties.Delegates

class TextMessageValidator<State>() : TelegramLogicValidator<State> {
    override fun validate(input: TelegramInput, state: State): Boolean {
        return input.message?.text != null
    }
}

class TextMessageLogicStepBuilder<State>(): Builder<TelegramLogicStep<State>> {
    var key: LogicStepKey? = null
    var validate: TelegramLogicValidator<State>? = null
    var process: TelegramLogicStepResultBuilder<State>.(input: TelegramInput, state: State, message: TextMessage) ->
    Unit by Delegates.notNull()

    override fun build(): TelegramLogicStep<State> {
        val combinedValidator = if (validate == null) TextMessageValidator()
        else CombinedValidator(TextMessageValidator(), validate!!)

        return BasicLogicStep(
            key ?: UUID.randomUUID().toString(),
            combinedValidator,
        ) { input, state -> process(input, state, TextMessage(input.message!!)) }
    }
}