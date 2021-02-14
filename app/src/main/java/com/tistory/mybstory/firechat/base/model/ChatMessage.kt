package com.tistory.mybstory.firechat.base.model

import com.google.firebase.Timestamp

data class ChatMessage(
    val id: String = "",
    val timestamp: Timestamp? = null,
    val uid: String = "",
    val value: String = ""
)
