package input

import kotlinx.coroutines.flow.Flow
import telegram.input.Update

interface TelegramInput {
    val inputFlow: Flow<Update>
    fun stop()
}