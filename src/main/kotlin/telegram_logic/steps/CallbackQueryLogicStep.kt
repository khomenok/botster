package telegram_logic.steps

import common.Builder
import logic.steps.BasicLogicStep
import logic.steps.CombinedValidator
import logic.steps.LogicStep
import logic.steps.LogicStepKey
import telegram.input.CallbackQuery
import telegram.input.TelegramInput
import telegram.output.OutputEntity
import telegram_logic.*
import java.util.*
import kotlin.properties.Delegates

class CallbackQueryValidator<State> : TelegramLogicValidator<State> {
    override fun validate(input: TelegramInput, state: State): Boolean {
        return input.callbackQuery?.id != null
    }
}

class CallbackQueryLogicStepBuilder<State>: Builder<TelegramLogicStep<State>> {
    var key: LogicStepKey? = null
    var validate: TelegramLogicValidator<State>? = null
    var process: TelegramLogicStepResultBuilder<State>.(input: TelegramInput, state: State, callbackQuery: CallbackQuery) ->
    Unit by Delegates.notNull()

    private val initValidator = CallbackQueryValidator<State>()

    override fun build(): LogicStep<State, TelegramInput, OutputEntity> {
        val combinedValidator = if (validate == null) initValidator else CombinedValidator(initValidator, validate!!)

        return BasicLogicStep(
            key ?: UUID.randomUUID().toString(),
            combinedValidator,
        ) { input, state -> process(input, state, input.callbackQuery!!) }
    }
}