/**
 * @filename:MessageRecvHandler.java
 *
 * Newland Co. Ltd. All rights reserved.
 *
 * @Description:Rpc服务器消息处理
 * @author tangjie
 * @version 1.0
 *
 */
package com.rpc.core;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Map;

import com.rpc.model.MessageRequest;
import com.rpc.model.MessageResponse;

/**
 * 对接收到的消息进行处理的handler类
 * @author PanTian
 *
 */
public class MessageRecvHandler extends ChannelInboundHandlerAdapter {

	//进行消息处理的类的参数集合
    private final Map<String, Object> handlerMap;

    public MessageRecvHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	//channelRead方法读取到的msg 肯定是自定义的消息请求类MessageRequest
        MessageRequest request = (MessageRequest) msg;
        //使用自定义的消息返回类,封装结果
        MessageResponse response = new MessageResponse();
        MessageRecvInitializeTask recvTask = new MessageRecvInitializeTask(request, response, handlerMap);
        MessageRecvExecutor.submit(recvTask, ctx, request, response);
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
}
