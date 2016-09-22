/**
 * @filename:MessageRecvChannelInitializer.java
 *
 * Newland Co. Ltd. All rights reserved.
 *
 * @Description:Rpc服务端管道初始化
 * @author tangjie
 * @version 1.0
 *
 */
package com.rpc.core;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

import java.util.Map;

import com.rpc.serialize.support.RpcSerializeProtocol;

/**
 * 自定义消息接收通道初始化类
 * @author PanTian
 *
 */
public class MessageRecvChannelInitializer extends ChannelInitializer<SocketChannel> {

    private RpcSerializeProtocol protocol;
    //rpc消息接收序列化框架.就是根据序列化类型选择合适的解码操作类
    private RpcRecvSerializeFrame frame = null;

    MessageRecvChannelInitializer buildRpcSerializeProtocol(RpcSerializeProtocol protocol) {
        this.protocol = protocol;
        return this;
    }

    MessageRecvChannelInitializer(Map<String, Object> handlerMap) {
        frame = new RpcRecvSerializeFrame(handlerMap);
    }

    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        frame.select(protocol, pipeline);
    }
}
