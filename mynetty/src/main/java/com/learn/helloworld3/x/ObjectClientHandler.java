package com.learn.helloworld3.x;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 * 自定义handler用于处理客户端发送消息时需要进行的处理
 * 基本上都是继承SimpleChannelHandler 就够了
 * 里面包含了对象发送和接收处理阶段的方法,供我们进行重新
 * 实现自己的业务逻辑
 * @author PanTian
 *
 */
public class ObjectClientHandler extends SimpleChannelHandler{
	
	/**
	 * 当绑定到服务端的时候出发该方法
	 * @param ctx
	 * @param e
	 */
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		// 向服务端发送Object信息
		sendObject(e.getChannel());
	}
	
	/**
	 * 消息通过事件ChannelStateEvent所对应的channel来发送消息
	 * @param channel
	 */
	public void sendObject(Channel channel){
		Command command = new Command();
		command.setActionName("client Action Name");
		//消息写入channel,在channel里面流转,让ChannelStateEvent事件处理
		channel.write(command);
	}
	

}
