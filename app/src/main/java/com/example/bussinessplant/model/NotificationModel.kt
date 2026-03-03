package com.example.bussinessplant.model

data class NotificationModel(
    var id: String = "",
    var title: String = "",
    var message: String = "",
    var timestamp: Long = System.currentTimeMillis()
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "title" to title,
            "message" to message,
            "timestamp" to timestamp
        )
    }
}
