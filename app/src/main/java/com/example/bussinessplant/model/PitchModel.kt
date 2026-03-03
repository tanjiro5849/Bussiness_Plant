package com.example.bussinessplant.model

data class PitchModel(
    var id: String = "",
    var startupName: String = "",
    var description: String = "",
    var category: String = "Technology", // Added category
    var userId: String = "",
    var timestamp: Long = System.currentTimeMillis() // Added timestamp for sorting
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "startupName" to startupName,
            "description" to description,
            "category" to category,
            "userId" to userId,
            "timestamp" to timestamp
        )
    }
}
