package com.learn.helloworld4.x;

import io.netty.channel.ChannelInboundHandlerAdapter;

import org.jboss.netty.channel.ChannelHandlerContext;

public class HelloServerHandler extends ChannelInboundHandlerAdapter {

	/**
	 * 覆盖channelActive方法
	 * 当客户端绑定到服务端的时候触发
	 */
     public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Hello world, I'm server.");
    }
	
}
