package com.rpc.model;

import java.util.Map;

/**
 * rpc服务映射容器,用来进行消息和返回结果的关系映射
 * @author PanTian
 *
 */
public class MessageKeyVal {

    private Map<String, Object> messageKeyVal;

    public void setMessageKeyVal(Map<String, Object> messageKeyVal) {
        this.messageKeyVal = messageKeyVal;
    }

    public Map<String, Object> getMessageKeyVal() {
        return messageKeyVal;
    }
}
