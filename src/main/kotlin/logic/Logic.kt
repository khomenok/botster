package logic

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import logic.steps.*

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