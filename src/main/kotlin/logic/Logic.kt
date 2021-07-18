package logic

import common.Builder
import common.builderFun
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import logic.step.*
import telegram.output.OutputEntity
import telegram.input.Update
import kotlin.properties.Delegates

// messages from one sender in different chats probably should go to different logic entities
fun Update.logicSenderHash() = "${this.senderChatId()}-${this.senderId()}"

class Logic<State>(
    private val steps: Map<LogicStepKey, LogicStep<State>>,
    private val entrypoints: List<LogicStepKey>,
    private var initialState: () -> State,
) {
    private val logicEntitiesByHash = mutableMapOf<String, LogicEntity<State>>()

    private val _outputFlow = MutableSharedFlow<OutputEntity>()
    val outputFlow = _outputFlow.asSharedFlow()


    suspend fun process(update: Update): LogicStepResult<State> {
        val senderHash = update.logicSenderHash()
        if (!logicEntitiesByHash.contains(senderHash)) {
            logicEntitiesByHash[senderHash] = LogicEntity(steps, entrypoints, initialState())
        }
        val result = logicEntitiesByHash[senderHash]!!.process(update)
        result.output.forEach { _outputFlow.emit(it) }
        return result
    }
}

class LogicBuilder<State>(): Builder<Logic<State>> {
    private val steps = mutableMapOf<LogicStepKey, LogicStep<State>>()
    private val entrypoints = mutableListOf<LogicStepKey>()
    var initialState: () -> State by Delegates.notNull()

    private fun <B : Builder<LogicStep<State>>> buildStep(builder: B, init: B.() -> Unit): LogicStep<State> {
        val builtStep = builderFun(builder, init)
        // TODO: throw duplicated keys
        steps[builtStep.key] = builtStep
        return builtStep
    }

    fun step(init: BasicLogicStepBuilder<State>.() -> Unit)= buildStep(BasicLogicStepBuilder(), init)
    fun textMessageStep(init: TextMessageLogicStepBuilder<State>.() -> Unit) = buildStep(TextMessageLogicStepBuilder(), init)
    fun callbackQueryStep(init: CallbackQueryLogicStepBuilder<State>.() -> Unit) = buildStep(CallbackQueryLogicStepBuilder(), init)

    fun entrypoint(step: LogicStep<State>) {
        entrypoints.add(step.key)
    }

    override fun build(): Logic<State> {
        return Logic(steps.toMap(), entrypoints.toList(), initialState)
    }
}

fun <State>logic(init: LogicBuilder<State>.() -> Unit): Logic<State> = builderFun(LogicBuilder(), init)
