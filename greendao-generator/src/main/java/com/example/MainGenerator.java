package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class MainGenerator {

    public static void main(String[] args) throws Exception{
        Schema schema = new Schema(1,"com.example.rujul.pushnotifications.database");
        schema.enableKeepSectionsByDefault();

        Entity group = schema.addEntity("Group");
        group.addStringProperty("groupId").primaryKey();
        group.addStringProperty("groupName");
        group.addStringProperty("topicArn");

        Entity chat = schema.addEntity("chat");
        chat.addIdProperty();
        chat.addStringProperty("message");
        chat.addStringProperty("from");
        chat.addDateProperty("at");

        Property grpId = chat.addStringProperty("groupId").getProperty();
        chat.addToOne(group, grpId);

        ToMany chatTogroup = group.addToMany(chat,grpId);
        chatTogroup.setName("chats");

        DaoGenerator dao = new DaoGenerator();
        dao.generateAll(schema, "./app/src/main/java");
    }
}
