package com.learn.helloworld3.x;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ClassResolvers;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;

/**
 * netty服务端代码
 * 
 * @author PanTian
 *
 */
public class HelloServer {

	public static void main(String args[]) {
		// Server服务启动器
		// 服务启动引导类:Bootstrap
		ServerBootstrap bootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));
		// 设置一个处理客户端消息和各种消息事件的类(Handler)
		/*bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(new HelloServerHandler());
			}
		});*/
		//自定义消息的decoder和encoder
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				//既然是服务端,这里当然是用解码操作了
				return Channels.pipeline(
						new ObjectDecoder(ClassResolvers.cacheDisabled(this.getClass().getClassLoader())),
						new ObjectServerHandler() 
						);
			}
		});
		
		
		// 开放8000端口供客户端访问。
		bootstrap.bind(new InetSocketAddress(8000));
		System.out.println("服务端启动成功 端口:8000!!");
	}

}