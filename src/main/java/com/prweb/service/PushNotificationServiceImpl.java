package com.prweb.service;

import com.prweb.util.APICloudPushService;
import org.springframework.stereotype.Service;


@Service
public class PushNotificationServiceImpl {



    //发送推送消息 accounts  phone ,分隔
    public void SendPushNotificationToAccounts(String basePath, String event, String title, String content, String userIds) {
        //发消息
        APICloudPushService.SendPushNotification(basePath, title, content, "1", "0", "", userIds);
    }

}
