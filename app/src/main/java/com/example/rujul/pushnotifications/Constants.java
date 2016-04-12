package com.example.rujul.pushnotifications;

/**
 * Created by rujul on 4/3/2016.
 */
public interface Constants {
  String    SENT_TOKEN_TO_SERVER = "sentTokenToServer",
            ID="id",
            REGISTRATION_COMPLETE = "registration_complete",
            TOKEN = "Oz32z2iXO82k7YNos9Fb",
            TOKEN_NAME="token",
            TAG = "VolleyTag",
            SHARED_FILE_NAME = "amazon_push_file",
            DEFAULT = "default",
            MOBILE="mobile",
            NAME="name",
            USER_EXIST="isUser",
            TEMP_NAME="temp_name",
            TEMP_MOBILE="temp_mobile",
            GCM = "gcm",
            GCM_TO_SEND="GCM",
            BASE_URL = "http://52.32.71.196:1337",
            SIGNUP_URL=BASE_URL+"/signup",
            CHAT_URL = BASE_URL+"/chat/new",
            ANAME = "android_name",
            AMOBILE = "android_mobile",
            DATA="data",
            DB_NAME="amazonchats",
            ALPHA_NUM="^[a-zA-Z0-9]*$",
            ALPHA_NUM_ERROR = "Please enter only alphabets or numbers",
            ADD_OR_JOIN_URL=BASE_URL+"/group/new",
            GROUP="group",
            GROUP_NAME="groupName",
            USER = "user",
            MESSAGE = "message",
            AT = "at",
            DATE_FORMAT="yyyy/MM/dd HH:mm:ss",
            TOPIC_ARN = "topicArn",
            REGION = "us-west-2",
            MESSAGE_AT="messageAt",
            MESSAGE_FROM="messageFrom",
            MESSAGE_TO="messageTo",
            MESSAGE_GROUP_ID="messageGroupId",
            NOTIFICATIONS = "notification_content",
            PUSH_NOTIFICATION="push_notification",
            WHOLE_MESSAGE = "whole_message";

     int    MESSAGE_NOTIFICATION_ID = 435345,
            PLAY_SERVICES_RESOLUTION_REQUEST=9000;
    long  SPLASH_TIME_OUT = 1500;
}
