package input

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import telegram.Api
import telegram.input.Update
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

class TelegramPollingInput(
    private val api: Api,
    private val delayInMilliseconds: Int = 300,
): TelegramInput {
    private var canPoll = true

    @OptIn(ExperimentalTime::class)
    override val inputFlow: Flow<Update> = flow {
        while (canPoll) {
            val updates = api.getUpdates()
            if (updates.ok) {
                updates.result.forEach { emit(it) }
            }

            delay(Duration.milliseconds(delayInMilliseconds))
        }
    }

    override fun stop() {
        canPoll = false
    }
}