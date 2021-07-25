package telegram.flow

import common.FlowInput
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import telegram.Api
import telegram.input.TelegramInput
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

class TelegramPollingInput(
    private val api: Api,
    private val delayInMilliseconds: Int = 300,
): FlowInput<TelegramInput> {
    private var canPoll = true

    private val _inputFlow = MutableSharedFlow<TelegramInput>()
    override val inputFlow = _inputFlow.asSharedFlow()

    @OptIn(ExperimentalTime::class)
    override suspend fun setupFlow() {
        canPoll = true
        while (canPoll) {
            val updates = api.getUpdates()
            if (updates.ok) {
                updates.result.forEach { _inputFlow.emit(it) }
            }

            delay(Duration.milliseconds(delayInMilliseconds))
        }
    }

    override fun stop() {
        canPoll = false
    }
}