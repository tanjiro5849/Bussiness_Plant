package com.example.bussinessplant.repository

import com.example.bussinessplant.model.NotificationModel

interface NotificationRepo {
    fun addNotification(notification: NotificationModel, callback: (Boolean, String) -> Unit)
    fun getAllNotifications(callback: (Boolean, List<NotificationModel>?, String) -> Unit)
    fun updateNotification(notificationId: String, data: Map<String, Any?>, callback: (Boolean, String) -> Unit)
    fun deleteNotification(notificationId: String, callback: (Boolean, String) -> Unit)
}
