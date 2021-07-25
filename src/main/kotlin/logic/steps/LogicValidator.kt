package logic.steps

interface LogicValidator<State, Input> {
    fun validate(input: Input, state: State): Boolean
}

class DummyValidator<State, Input> : LogicValidator<State, Input> {
    override fun validate(input: Input, state: State): Boolean = true
}

class CombinedValidator<State, Input> (
    private vararg val validators: LogicValidator<State, Input>,
) : LogicValidator<State, Input> {
    override fun validate(input: Input, state: State): Boolean {
        return validators.firstOrNull { !it.validate(input, state) } != null
    }
}