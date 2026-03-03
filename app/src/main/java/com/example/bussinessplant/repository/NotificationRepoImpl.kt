package com.example.bussinessplant.repository

import com.example.bussinessplant.model.NotificationModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NotificationRepoImpl : NotificationRepo {
    private val database = FirebaseDatabase.getInstance()
    private val ref = database.getReference("Notifications")

    override fun addNotification(notification: NotificationModel, callback: (Boolean, String) -> Unit) {
        val id = ref.push().key.toString()
        notification.id = id
        ref.child(id).setValue(notification).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Notification added successfully")
            } else {
                callback(false, it.exception?.message.toString())
            }
        }
    }

    override fun getAllNotifications(callback: (Boolean, List<NotificationModel>?, String) -> Unit) {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val notifications = mutableListOf<NotificationModel>()
                    for (child in snapshot.children) {
                        val model = child.getValue(NotificationModel::class.java)
                        if (model != null) {
                            notifications.add(model)
                        }
                    }
                    callback(true, notifications, "Notifications fetched successfully")
                } else {
                    callback(true, emptyList(), "No notifications found")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, null, error.message)
            }
        })
    }

    override fun updateNotification(notificationId: String, data: Map<String, Any?>, callback: (Boolean, String) -> Unit) {
        ref.child(notificationId).updateChildren(data).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Notification updated successfully")
            } else {
                callback(false, it.exception?.message.toString())
            }
        }
    }

    override fun deleteNotification(notificationId: String, callback: (Boolean, String) -> Unit) {
        ref.child(notificationId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Notification deleted successfully")
            } else {
                callback(false, it.exception?.message.toString())
            }
        }
    }
}
