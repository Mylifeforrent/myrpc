package com.learn.helloworld4.x;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;



/**
 * 服务端
 * @author PanTian
 *
 */
public class HelloWorldServer {

	public static void main(String[] args) {
		
		//// EventLoop 代替原来的 ChannelFactory
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // server端采用简洁的连写方式，client端才用分段普通写法。
           serverBootstrap.group(bossGroup, workerGroup)
                     .channel(NioServerSocketChannel. class )
                     .childHandler( new ChannelInitializer<SocketChannel>() {
                           @Override
                           public void initChannel(SocketChannel ch)
                                    throws Exception {
                               ch.pipeline().addLast( 
                            		   new HelloServerHandler());
                          }
                     }).option(ChannelOption. SO_KEEPALIVE , true );

           ChannelFuture f = serverBootstrap.bind(8000).sync();
           f.channel().closeFuture().sync();
       } catch (InterruptedException e) {
    	   e.printStackTrace();
       } finally {
           workerGroup.shutdownGracefully();
           bossGroup.shutdownGracefully();
       }
		
		
		
	}
	
}
