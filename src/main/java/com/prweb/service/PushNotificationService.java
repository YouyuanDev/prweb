package com.prweb.service;

public interface PushNotificationService {

    //发送推送消息 accounts  phone ,分隔
    public void SendPushNotificationToAccounts(String basePath, String event, String title, String content, String userIds);
}