package com.rpc.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * rpc服务应答消息结构
 * @author PanTian
 *
 */
public class MessageResponse implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2553559427559365917L;
	private String messageId;//对应的消息id
    private String error;//错误信息
    private Object resultDesc;//返回结果

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getResult() {
        return resultDesc;
    }

    public void setResult(Object resultDesc) {
        this.resultDesc = resultDesc;
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("messageId", messageId).append("error", error).toString();
    }
}