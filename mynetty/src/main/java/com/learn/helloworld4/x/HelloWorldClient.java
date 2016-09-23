package com.learn.helloworld4.x;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * Netty4客户端代码
 * @author PanTian
 *
 */
public class HelloWorldClient {
	
	
	/**
	 * NioEventLoopGroup 是一个处理I/O操作的多线程事件环。
		即为Netty4里的线程池，在3.x里，一个Channel是由ChannelFactory创建的，
		同时新创建的Channel会自动注册到一个隐藏的I/O线程。 
		4.0使用新的名为EventLoopGroup的接口来替换ChannelFactory，
		它由一个或多个EventLoop来构成。一个新的 Channel不会自动注册到EventLoopGroup，
		但用户可以显式调用EventLoopGroup.register（）来注册。
		在Server端的Bootstrap参数中，有两个EventLoopGroup，
		第一个通常称为'boss'，用于接收发来的连接请求。第二个称为'worker',，
		用于处理boss接受并且注册给worker的连接中的信息。
		ChannelInitializer是一个特殊的handler，用于方便的配置用户自定义的handler实现，
		如代码中所示。在channelRegistered的生命周期中会触发用户复写的initChannel(C ch)方法，
		并且在调用后会讲自身从channelPipeline中移除。
	 * @param args
	 */
	public static void main(String args[]) {
		
        // Client服务启动器 3.x的ClientBootstrap 改为Bootstrap，且构造函数变化很大，这里用无参构造。
       Bootstrap bootstrap = new Bootstrap();
        // 指定channel类型
       bootstrap.channel(NioSocketChannel. class );
        // 指定Handler
       bootstrap.handler( new HelloClientHandler());
        // 指定EventLoopGroup
       bootstrap.group( new NioEventLoopGroup());
        // 连接到本地的8000端口的服务端
       bootstrap.connect( new InetSocketAddress("127.0.0.1" , 18887));
       System.out.println("客户端启动ok!!");
       
  }

}
