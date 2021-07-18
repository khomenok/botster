package telegram.input

import telegram.ChatId

interface InputEntity {
    fun senderChatId(): ChatId
    fun senderId(): ChatId
}