package com.learn.helloworld3.x;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 * 自定义服务端接收信息之后处理流程的handler 继承SimpleChannelHandler,然后覆盖对应的方法即可
 * 
 * @author PanTian
 *
 */
public class ObjectServerHandler extends SimpleChannelHandler {

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		//既然数据是以该对象进行序列化之后传进来服务端的,那么久一定可以通过接收到的消息,强制转换为Command
		Command command = (Command)e.getMessage();
		ctx.sendUpstream(e);
		System.out.println("接收到消息:"+command.getActionName());
	}

}
