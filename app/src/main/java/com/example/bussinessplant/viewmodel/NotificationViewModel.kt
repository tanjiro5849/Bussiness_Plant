package com.example.bussinessplant.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bussinessplant.model.NotificationModel
import com.example.bussinessplant.repository.NotificationRepo

class NotificationViewModel(val repo: NotificationRepo) : ViewModel() {
    private val _notifications = MutableLiveData<List<NotificationModel>?>()
    val notifications: MutableLiveData<List<NotificationModel>?> = _notifications

    private val _loading = MutableLiveData<Boolean>()
    val loading: MutableLiveData<Boolean> = _loading

    fun addNotification(notification: NotificationModel, callback: (Boolean, String) -> Unit) {
        repo.addNotification(notification, callback)
    }

    fun getAllNotifications() {
        _loading.postValue(true)
        repo.getAllNotifications { success, data, message ->
            _loading.postValue(false)
            if (success) {
                _notifications.postValue(data)
            }
        }
    }

    fun updateNotification(notificationId: String, data: Map<String, Any?>, callback: (Boolean, String) -> Unit) {
        repo.updateNotification(notificationId, data, callback)
    }

    fun deleteNotification(notificationId: String, callback: (Boolean, String) -> Unit) {
        repo.deleteNotification(notificationId, callback)
    }
}
