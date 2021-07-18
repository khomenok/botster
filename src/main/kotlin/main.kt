import input.TelegramPollingInput
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import logic.logic
import telegram.*
import telegram.output.InlineKeyboard
import telegram.output.OutputMessage
import telegram.output.TextInlineKeyboardButton

typealias State = Int

fun main(args: Array<String>) {
    runBlocking {

        val botLogic = logic<State> {
            initialState = { 10 }

            val subStep = textMessageStep {
                process = { update, state, message ->
                    nextState = state - (message.text.toIntOrNull() ?: 0)
                    //addOutput(OutputMessage(update.senderChatId(), "Вычел из $state: $nextState"))
                    addOutput(
                        OutputMessage(
                        update.senderChatId(),
                        "3/15: Dog (sub.)\n\n- Type in german\n- Select gender",
                        replyMarkup = InlineKeyboard(listOf(listOf(
                            TextInlineKeyboardButton("die", "plus"),
                            TextInlineKeyboardButton("der", "plus"),
                            TextInlineKeyboardButton("das", "minus"),
                            TextInlineKeyboardButton("plural", "minus"),
                        ), listOf(
                            TextInlineKeyboardButton("\uD83E\uDDE0", "minus"),
                            TextInlineKeyboardButton("\uD83C\uDFA7", "minus"),
                            TextInlineKeyboardButton("\uD83D\uDC40", "minus"),
                        )))
                    )
                    )
                }
            }

            val addStep = textMessageStep {
                process = { update, state, message -> // todo: flip update and message
                    nextState = state + (message.text.toIntOrNull() ?: 0)
                    addOutput(
                        OutputMessage(
                        update.senderChatId(),
                        "Прибавил к $state: $nextState",
                        replyMarkup = InlineKeyboard(listOf(listOf(
                            TextInlineKeyboardButton("Plus", "plus"),
                            TextInlineKeyboardButton("Minus", "minus"),
                        )))
                    )
                    )
                    nextStep(subStep)
                }
            }

            val addCallbackQuery = callbackQueryStep {
                process = { update, state, callbackQuery ->
                    if (callbackQuery.data == "plus") {
                        nextState = state + 1
                        addOutput(OutputMessage(update.senderChatId(), "Прибавил единичку к $state: $nextState"))
                    }
                    if (callbackQuery.data == "minus") {
                        nextState = state - 1
                        addOutput(OutputMessage(update.senderChatId(), "Вычел единичку из $state: $nextState"))
                    }
                }
            }

            entrypoint(addStep)
            entrypoint(addCallbackQuery)
        }

        val telegramApi = Api("1757729023:AAHYSp5BS7_dLLOtq8Pu1p3Dm8eySKdi9m0")
        val telegramInput = TelegramPollingInput(telegramApi)

        launch {
            botLogic.outputFlow.collect {
                it.getSent(telegramApi)
            }
        }
        launch {
            telegramInput.inputFlow.collect {
                botLogic.process(it)
            }
        }
    }
}
