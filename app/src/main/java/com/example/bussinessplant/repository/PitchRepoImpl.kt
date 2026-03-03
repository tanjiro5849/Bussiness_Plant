package com.example.bussinessplant.repository

import com.example.bussinessplant.model.PitchModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PitchRepoImpl : PitchRepo {
    private val database = FirebaseDatabase.getInstance()
    private val ref = database.getReference("Pitches")

    override fun addPitch(pitch: PitchModel, callback: (Boolean, String) -> Unit) {
        val id = ref.push().key.toString()
        pitch.id = id
        ref.child(id).setValue(pitch).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Pitch added successfully")
            } else {
                callback(false, it.exception?.message.toString())
            }
        }
    }

    override fun getAllPitches(callback: (Boolean, List<PitchModel>?, String) -> Unit) {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val pitches = mutableListOf<PitchModel>()
                    for (child in snapshot.children) {
                        val model = child.getValue(PitchModel::class.java)
                        if (model != null) {
                            pitches.add(model)
                        }
                    }
                    callback(true, pitches, "Pitches fetched successfully")
                } else {
                    callback(true, emptyList(), "No pitches found")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, null, error.message)
            }
        })
    }

    override fun updatePitch(pitchId: String, data: Map<String, Any?>, callback: (Boolean, String) -> Unit) {
        if (pitchId.isEmpty()) {
            callback(false, "Invalid Pitch ID")
            return
        }
        ref.child(pitchId).updateChildren(data).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Pitch updated successfully")
            } else {
                callback(false, it.exception?.message.toString())
            }
        }
    }

    override fun deletePitch(pitchId: String, callback: (Boolean, String) -> Unit) {
        if (pitchId.isEmpty()) {
            callback(false, "Invalid Pitch ID")
            return
        }
        ref.child(pitchId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Pitch deleted successfully")
            } else {
                callback(false, it.exception?.message.toString())
            }
        }
    }
}
