package com.example.bussinessplant.repository

import com.example.bussinessplant.model.PitchModel

interface PitchRepo {
    fun addPitch(pitch: PitchModel, callback: (Boolean, String) -> Unit)
    fun getAllPitches(callback: (Boolean, List<PitchModel>?, String) -> Unit)
    fun updatePitch(pitchId: String, data: Map<String, Any?>, callback: (Boolean, String) -> Unit)
    fun deletePitch(pitchId: String, callback: (Boolean, String) -> Unit)
}
