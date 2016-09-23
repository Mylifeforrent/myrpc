package com.learn.helloworld3.x;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;

/**
 * 在《Java NIO框架Netty教程（五）- 消息收发次数不匹配的问题》里我们试图分析一个消息收发次数不匹配的问题。
当时笔者还是心存疑惑的。所以决定先学习一下Java NIO的Selector机制。
经过简单的了解，笔者大胆的猜测和”武断”一下该问题的原因。
首先，Selector机制让我们注册一个感兴趣的事件，然后只要有该事件发生，就会传递给接收端。我们写了三次，接收端一定会出发三次的。
然后，Netty实现机制里，有个Buffer缓冲池，把收到的信息都缓存在里面，通过一个线程统一处理。也就是我们看到的那个buffer的处理过程。
Netty的设置中，有一个一次性最多读取字节大小的设定。并且，事件的触发是在处理过缓冲池中的消息之后。我们再来回顾一下Netty中读取信息的那段代码：
可以看到，如果没有读取到字节是不会触发事件的，所以我们可能会收到2次或者3次信息。
（如果发的快，解析的慢，后两次信息，一次性读取了，就2次，如果发送间隔长，分次解析，就收到3次。）原因应该就是如此。跟我们开始猜的差不多，只是不敢确认。
 */
/**
 * 客户端
 * @author PanTian
 *
 */
public class HelloClient {  
	  
    public static void main(String args[]) {  
        // Client服务启动器  
        ClientBootstrap bootstrap = new ClientBootstrap(  
                new NioClientSocketChannelFactory(  
                        Executors.newCachedThreadPool(),  
                        Executors.newCachedThreadPool()));  
        // 设置一个处理服务端消息和各种消息事件的类(Handler)  
        /**
         * 说了这么多废话，才提到对象的传输，不知道您是不是已经不耐烦了。
			一个系统内部的消息传递，没有对象传递是不太现实的。下面就来说说，怎么传递对象。
			如果，您看过前面的介绍，如果您善于专注本质，勤于思考。您应该也会想到，
			我们说过，Netty的消息传递都是基于流，通过ChannelBuffer传递的，那么自然，
			Object也需要转换成ChannelBuffer来传递。好在Netty本身已经给我们写好了这样的转换工具。
			ObjectEncoder和ObjectDecoder。
			工具怎么用？再一次说说所谓的本质，我们之前也说过，Netty给我们处理自己业务的空间是在灵活的可子定义的Handler上的，
			也就是说，如果我们自己去做这个转换工作，那么也应该在Handler里去做。而Netty，
			提供给我们的ObjectEncoder和Decoder也恰恰是一组Handler。于是，修改Server和Client的启动代码：
         */
       /* bootstrap.setPipelineFactory(new ChannelPipelineFactory() {  
            public ChannelPipeline getPipeline() throws Exception {  
                return Channels.pipeline(new HelloClientHandler());  
            }  
        }); */ 
        //自定义客户端对象的编码解码操作,和处理的handler
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			
        	//这里是客户端,当然是用编码操作ObjectEncoder
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(
										new ObjectEncoder(),
										new ObjectClientHandler());
			}
		});
        
        // 连接到本地的8000端口的服务端  
        bootstrap.connect(new InetSocketAddress(  
                "127.0.0.1", 8000));  
        
        /**
         * 简单梳理一下思路：
			通过Netty传递，都需要基于流，以ChannelBuffer的形式传递。
			所以，Object -> ChannelBuffer.
			Netty提供了转换工具，需要我们配置到Handler。
			样例从客户端 -> 服务端，单向发消息，所以在客户端配置了编码，
			服务端解码。如果双向收发，则需要全部配置Encoder和Decoder。
			这里需要注意，注册到Server的Handler是有顺序的，如果你颠倒一下注册顺序：
			结果就是，会先进入我们自己的业务，再进行解码。这自然是不行的，会强转失败。至此，你应该会用Netty传递对象了吧
         */
    }  
  
}  
