package com.learn.helloworld3.x;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 * 
 * 在TCP/IP这种基于流传递的协议中。他识别的不是你每一次发送来的消息，不是分包的。而是，只认识一个整体的流，
 * 即使分三次分别发送三段话：ABC、DEF、GHI。在传递的过程中，他就是一个具有整体长度的流。在读流的过程中，
 * 如果我一次读取的长度选择的不是三个，
 * 我可以收到类似AB、CDEFG、H、I这样的信息。这显然是我们不想看到的。所以说，
 * 在你写的消息收发的系统里，需要预先定义好这种解析机制
 * 当有客户端绑定到服务端的时候触发，打印"Hello world, I'm server."
 * @author PanTian
 *
 */
public class HelloServerHandler extends SimpleChannelHandler {

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		//System.out.println("Hello world, I'm server.");
		String msg = "hello i'm client";
		//得到一个buffer,用来作为传输object
		ChannelBuffer buffer = ChannelBuffers.buffer(msg.length());
		buffer.writeBytes(msg.getBytes());
		//发送以对象封装的消息,其实就是字节数组
		e.getChannel().write(buffer);
	}
}
