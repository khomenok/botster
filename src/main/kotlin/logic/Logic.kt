package logic

import common.Builder
import common.builderFun
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import logic.step.*
import telegram.output.OutputEntity
import telegram.input.Update
import kotlin.properties.Delegates

interface LogicInput {
    fun logicSenderHash(): String
}

class Logic<State, Input : LogicInput, Output>(
    private val steps: Map<LogicStepKey, LogicStep<State, Input, Output>>,
    private val entrypoints: List<LogicStepKey>,
    private var initialState: () -> State,
) {
    private val logicEntitiesByHash = mutableMapOf<String, LogicEntity<State, Input, Output>>()

    private val _outputFlow = MutableSharedFlow<Output>()
    val outputFlow = _outputFlow.asSharedFlow()


    suspend fun process(update: Input): LogicStepResult<State, Input, Output> {
        val senderHash = update.logicSenderHash()
        if (!logicEntitiesByHash.contains(senderHash)) {
            logicEntitiesByHash[senderHash] = LogicEntity(steps, entrypoints, initialState())
        }
        val result = logicEntitiesByHash[senderHash]!!.process(update)
        result.output.forEach { _outputFlow.emit(it) }
        return result
    }
}

typealias TelegramLogic<State> = Logic<State, Update, OutputEntity>

// todo: move out of the logic, cause this thing now about telegram
class LogicBuilder<State>(): Builder<TelegramLogic<State>> {
    private val steps = mutableMapOf<LogicStepKey, TelegramLogicStep<State>>()
    private val entrypoints = mutableListOf<LogicStepKey>()
    var initialState: () -> State by Delegates.notNull()

    private fun <B : Builder<TelegramLogicStep<State>>> buildStep(builder: B, init: B.() -> Unit): TelegramLogicStep<State> {
        val builtStep = builderFun(builder, init)
        // TODO: throw duplicated keys
        steps[builtStep.key] = builtStep
        return builtStep
    }

    fun step(init: BasicLogicStepBuilder<State, Update, OutputEntity>.() -> Unit)= buildStep(BasicLogicStepBuilder(), init)
    fun textMessageStep(init: TextMessageLogicStepBuilder<State>.() -> Unit) = buildStep(TextMessageLogicStepBuilder(), init)
    fun callbackQueryStep(init: CallbackQueryLogicStepBuilder<State>.() -> Unit) = buildStep(CallbackQueryLogicStepBuilder(), init)

    fun entrypoint(step: TelegramLogicStep<State>) {
        entrypoints.add(step.key)
    }

    override fun build(): TelegramLogic<State> {
        return Logic(steps.toMap(), entrypoints.toList(), initialState)
    }
}

// todo: move out of the logic, cause this thing now about telegram
fun <State>logic(init: LogicBuilder<State>.() -> Unit): TelegramLogic<State> = builderFun(LogicBuilder(), init)
