package logic.step

import common.Builder
import telegram.input.CallbackQuery
import telegram.input.Update
import java.util.*
import kotlin.properties.Delegates

class CallbackQueryValidator<State> : LogicValidator<State> {
    override fun validate(update: Update, state: State): Boolean {
        return update.callbackQuery?.id != null
    }
}

class CallbackQueryLogicStepBuilder<State>(): Builder<LogicStep<State>> {
    var key: LogicStepKey? = null
    var validate: LogicValidator<State>? = null
    var process: LogicStepResultBuilder<State>.(update: Update, state: State, callbackQuery: CallbackQuery) ->
    Unit by Delegates.notNull()

    private val initValidator = CallbackQueryValidator<State>()

    override fun build(): LogicStep<State> {
        val combinedValidator = if (validate == null) initValidator else CombinedValidator(initValidator, validate!!)

        return BasicLogicStep(
            key ?: UUID.randomUUID().toString(),
            combinedValidator,
        ) { update, state -> process(update, state, update.callbackQuery!!) }
    }
}