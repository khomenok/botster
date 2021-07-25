package input

import kotlinx.coroutines.flow.SharedFlow

interface FlowInput<Input> {
    suspend fun setupFlow() : SharedFlow<Input>
    fun stop()
}