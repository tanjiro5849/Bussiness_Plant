package com.example.bussinessplant.model

data class PitchModel(
    var id: String = "",
    var startupName: String = "",
    var description: String = "",
    var userId: String = ""
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "startupName" to startupName,
            "description" to description,
            "userId" to userId
        )
    }
}
