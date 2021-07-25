package input

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import telegram.Api
import telegram.input.Update
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

class TelegramPollingInput(
    private val api: Api,
    private val delayInMilliseconds: Int = 300,
): FlowInput<Update> {
    private var canPoll = true

    private val _inputFlow = MutableSharedFlow<Update>()
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