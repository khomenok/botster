package logic.step

import common.Builder
import telegram.input.CallbackQuery
import telegram.input.Update
import telegram.output.OutputEntity
import java.util.*
import kotlin.properties.Delegates

class CallbackQueryValidator<State> : TelegramLogicValidator<State> {
    override fun validate(update: Update, state: State): Boolean {
        return update.callbackQuery?.id != null
    }
}

// todo: move out of the logic, cause this thing now about telegram
class CallbackQueryLogicStepBuilder<State>: Builder<TelegramLogicStep<State>> {
    var key: LogicStepKey? = null
    var validate: TelegramLogicValidator<State>? = null
    var process: TelegramLogicStepResultBuilder<State>.(update: Update, state: State, callbackQuery: CallbackQuery) ->
    Unit by Delegates.notNull()

    private val initValidator = CallbackQueryValidator<State>()

    override fun build(): LogicStep<State, Update, OutputEntity> {
        val combinedValidator = if (validate == null) initValidator else CombinedValidator(initValidator, validate!!)

        return BasicLogicStep(
            key ?: UUID.randomUUID().toString(),
            combinedValidator,
        ) { update, state -> process(update, state, update.callbackQuery!!) }
    }
}