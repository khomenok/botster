package common

import kotlinx.coroutines.flow.SharedFlow

interface FlowInput<Input> {
    val inputFlow: SharedFlow<Input>
    suspend fun setupFlow()
    fun stop()
}