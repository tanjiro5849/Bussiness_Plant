package com.example.bussinessplant.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bussinessplant.model.PitchModel
import com.example.bussinessplant.repository.PitchRepo

class PitchViewModel(val repo: PitchRepo) : ViewModel() {
    private val _pitches = MutableLiveData<List<PitchModel>?>()
    val pitches: MutableLiveData<List<PitchModel>?> = _pitches

    private val _loading = MutableLiveData<Boolean>()
    val loading: MutableLiveData<Boolean> = _loading

    fun addPitch(pitch: PitchModel, callback: (Boolean, String) -> Unit) {
        repo.addPitch(pitch, callback)
    }

    fun getAllPitches() {
        _loading.postValue(true)
        repo.getAllPitches { success, data, message ->
            _loading.postValue(false)
            if (success) {
                _pitches.postValue(data)
            }
        }
    }

    fun updatePitch(pitchId: String, data: Map<String, Any?>, callback: (Boolean, String) -> Unit) {
        repo.updatePitch(pitchId, data, callback)
    }

    fun deletePitch(pitchId: String, callback: (Boolean, String) -> Unit) {
        repo.deletePitch(pitchId, callback)
    }
}
