package logic.step

import telegram.input.Update

interface LogicValidator<State> {
    fun validate(update: Update, state: State): Boolean
}

class DummyValidator<State> : LogicValidator<State> {
    override fun validate(update: Update, state: State): Boolean = true
}

class CombinedValidator<State> (
    private vararg val validators: LogicValidator<State>,
) : LogicValidator<State> {
    override fun validate(update: Update, state: State): Boolean {
        return validators.firstOrNull { !it.validate(update, state) } != null
    }
}