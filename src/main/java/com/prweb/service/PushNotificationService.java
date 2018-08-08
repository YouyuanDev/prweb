package com.prweb.service;

import com.alibaba.fastjson.JSONObject;

public interface PushNotificationService {

    //发送推送
    public void SendPushNotification(String basePath, JSONObject json, String orderNo, String event);

    //发送推送消息 accounts  phone ,分隔
    public void SendPushNotificationToAccounts(String basePath, String event, String title, String content, String userIds);
}