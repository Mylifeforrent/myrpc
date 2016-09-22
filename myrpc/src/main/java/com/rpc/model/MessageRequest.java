package com.rpc.model;

/**
 * @filename:MessageResponse.java
 *
 * Newland Co. Ltd. All rights reserved.
 *
 * @Description:rpc服务应答结构
 * @author tangjie
 * @version 1.0
 *
 */

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * rpc服务请求结构
 * @author PanTian
 *
 */
public class MessageRequest implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2798911281442747588L;
	private String messageId;//请求的消息id
    private String className;//请求调用的类名
    private String methodName;//请求调用的方法名
    private Class<?>[] typeParameters;//类型参数
    private Object[] parametersVal;//参数值

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getTypeParameters() {
        return typeParameters;
    }

    public void setTypeParameters(Class<?>[] typeParameters) {
        this.typeParameters = typeParameters;
    }

    public Object[] getParameters() {
        return parametersVal;
    }

    public void setParameters(Object[] parametersVal) {
        this.parametersVal = parametersVal;
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("messageId", messageId).append("className", className)
                .append("methodName", methodName).toString();
    }
}
