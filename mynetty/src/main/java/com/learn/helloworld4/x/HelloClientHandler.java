package com.learn.helloworld4.x;

import io.netty.channel.ChannelInboundHandlerAdapter;

import org.jboss.netty.channel.ChannelHandlerContext;

/**
 * 处理客户端发送的信息的handler
 * @author PanTian
 *
 */
public class HelloClientHandler extends ChannelInboundHandlerAdapter  {
	
	
	/**
	 * 当成功绑定到server端的时候,执行相关操作
	 */
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Hello world, I'm client.");
    }
	
	

}
