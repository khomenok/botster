package telegram_logic

import common.Builder
import common.builderFun
import logic.Logic
import logic.steps.*
import telegram.input.TelegramInput
import telegram.output.OutputEntity
import telegram_logic.steps.CallbackQueryLogicStepBuilder
import telegram_logic.steps.TextMessageLogicStepBuilder
import kotlin.properties.Delegates

typealias TelegramLogic<State> = Logic<State, TelegramInput, OutputEntity>
typealias TelegramLogicStep<State> = LogicStep<State, TelegramInput, OutputEntity>
typealias TelegramLogicStepResultBuilder<State> = LogicStepResultBuilder<State, TelegramInput, OutputEntity>
typealias TelegramLogicValidator<State> = LogicValidator<State, TelegramInput>

// todo: move out of the logic, cause this thing now about telegram
class TelegramLogicBuilder<State>(): Builder<TelegramLogic<State>> {
    private val steps = mutableMapOf<LogicStepKey, TelegramLogicStep<State>>()
    private val entrypoints = mutableListOf<LogicStepKey>()
    var initialState: () -> State by Delegates.notNull()

    private fun <B : Builder<TelegramLogicStep<State>>> buildStep(builder: B, init: B.() -> Unit): TelegramLogicStep<State> {
        val builtStep = builderFun(builder, init)
        // TODO: throw duplicated keys
        steps[builtStep.key] = builtStep
        return builtStep
    }

    fun step(init: BasicLogicStepBuilder<State, TelegramInput, OutputEntity>.() -> Unit)= buildStep(
        BasicLogicStepBuilder(), init)
    fun textMessageStep(init: TextMessageLogicStepBuilder<State>.() -> Unit) = buildStep(TextMessageLogicStepBuilder(), init)
    fun callbackQueryStep(init: CallbackQueryLogicStepBuilder<State>.() -> Unit) = buildStep(
        CallbackQueryLogicStepBuilder(), init)

    fun entrypoint(step: TelegramLogicStep<State>) {
        entrypoints.add(step.key)
    }

    override fun build(): TelegramLogic<State> {
        return Logic(steps.toMap(), entrypoints.toList(), initialState)
    }
}

// todo: move out of the logic, cause this thing now about telegram
fun <State>telegramLogic(init: TelegramLogicBuilder<State>.() -> Unit): TelegramLogic<State> = builderFun(TelegramLogicBuilder(), init)
