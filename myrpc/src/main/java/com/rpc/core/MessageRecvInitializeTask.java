/**
 * @filename:MessageRecvInitializeTask.java
 *
 * Newland Co. Ltd. All rights reserved.
 *
 * @Description:Rpc服务器消息线程任务处理
 * @author tangjie
 * @version 1.0
 *
 */
package com.rpc.core;

import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.reflect.MethodUtils;

import com.rpc.model.MessageRequest;
import com.rpc.model.MessageResponse;

/**
 * 对接收到的消息进行初始化任务执行,其实就是Callable类的实现
 * @author PanTian
 *
 */
public class MessageRecvInitializeTask implements Callable<Boolean> {

	/**
	 * 定义好对应的消息请求结构和消息返回结构
	 * 以及对应的参数
	 */
    private MessageRequest request = null;
    private MessageResponse response = null;
    private Map<String, Object> handlerMap = null;
    private ChannelHandlerContext ctx = null;

    public MessageResponse getResponse() {
        return response;
    }

    public MessageRequest getRequest() {
        return request;
    }

    public void setRequest(MessageRequest request) {
        this.request = request;
    }

    MessageRecvInitializeTask(MessageRequest request, MessageResponse response, Map<String, Object> handlerMap) {
        this.request = request;
        this.response = response;
        this.handlerMap = handlerMap;
        this.ctx = ctx;
    }

    public Boolean call() {
    	//设置返回数据的id,用来和请求数据id对应,因为是异步操作不然找不到应该返回给谁
        response.setMessageId(request.getMessageId());
        try {
            Object result = reflect(request);
            //返回结果放入响应体中
            response.setResult(result);
            return Boolean.TRUE;
        } catch (Throwable t) {
            response.setError(t.toString());
            t.printStackTrace();
            System.err.printf("RPC Server invoke error!\n");
            return Boolean.FALSE;
        }
    }

    /**
     * 采用反射方式进行方法调用
     * @param request
     * @return
     * @throws Throwable
     */
    private Object reflect(MessageRequest request) throws Throwable {
        String className = request.getClassName();
        Object serviceBean = handlerMap.get(className);
        String methodName = request.getMethodName();
        Object[] parameters = request.getParameters();
        return MethodUtils.invokeMethod(serviceBean, methodName, parameters);
    }
}
