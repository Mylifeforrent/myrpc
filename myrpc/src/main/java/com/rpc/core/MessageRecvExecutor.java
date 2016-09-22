/**
 * @filename:MessageRecvExecutor.java
 *
 * Newland Co. Ltd. All rights reserved.
 *
 * @Description:Rpc服务器执行模块
 * @author tangjie
 * @version 1.0
 *
 */
package com.rpc.core;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.rpc.model.MessageKeyVal;
import com.rpc.model.MessageRequest;
import com.rpc.model.MessageResponse;
import com.rpc.serialize.support.RpcSerializeProtocol;

/**
 * rpc服务端,用来接收rpc客户端请求
 * @author PanTian
 *
 */
public class MessageRecvExecutor implements ApplicationContextAware, InitializingBean {

    private String serverAddress;//服务发布地址
    
    //从枚举类中取出对应的序列化协议,这里采用java原生的序列化协议
    private RpcSerializeProtocol serializeProtocol = RpcSerializeProtocol.JDKSERIALIZE;

    private final static String DELIMITER = ":";//分隔符,用于对地址进行分割

    //采用concurrentHashMap 在rpc服务中多线程效率更高
    private Map<String, Object> handlerMap = new ConcurrentHashMap<String, Object>();

//    ListenableFuture的说明 :
//    	并发编程是一个难题，但是一个强大而简单的抽象可以显著的简化并发的编写。出于这样的考虑，Guava 定义了 ListenableFuture接口并继承了JDK concurrent包下的Future 接口，
//    	ListenableFuture 允许你注册回调方法(callbacks)，在运算（多线程执行）完成的时候进行调用,  或者在运算（多线程执行）完成后立即执行。
//    	这样简单的改进，使得可以明显的支持更多的操作，这样的功能在JDK concurrent中的Future是不支持的。 
//    	在高并发并且需要大量Future对象的情况下，推荐尽量使用ListenableFuture来代替..
//    	ListenableFuture 中的基础方法是addListener(Runnable, Executor), 该方法会在多线程运算完的时候，在Executor中执行指定的Runnable。
//    	ListenableFuture的创建和使用:
//    	对应JDK中的 ExecutorService.submit(Callable) 提交多线程异步运算的方式，Guava 提供了ListeningExecutorService 接口, 
//    	该接口返回 ListenableFuture， 而相应的ExecutorService 返回普通的 Future。
//    	将 ExecutorService 转为 ListeningExecutorService，可以使用MoreExecutors.listeningDecorator(ExecutorService)进行装饰
    private static ListeningExecutorService threadPoolExecutor;

    public MessageRecvExecutor(String serverAddress, String serializeProtocol) {
        this.serverAddress = serverAddress;
        this.serializeProtocol = Enum.valueOf(RpcSerializeProtocol.class, serializeProtocol);
    }

    public static void submit(Callable<Boolean> task, final ChannelHandlerContext ctx, final MessageRequest request, final MessageResponse response) {
        if (threadPoolExecutor == null) {
        	//加上同步锁
            synchronized (MessageRecvExecutor.class) {
                if (threadPoolExecutor == null) {
                	//如果用来执行并发操作的threadPoolExecutor为空,就采用MoreExecutors.listeningDecorator操作,来创建一个
                    threadPoolExecutor = MoreExecutors.listeningDecorator((ThreadPoolExecutor) RpcThreadPool.getExecutor(16, -1));
                }
            }
        }

        /**
         * 使用线程池执行器来进行任务的提交
         */
        ListenableFuture<Boolean> listenableFuture = threadPoolExecutor.submit(task);
        //给listenableFuture运行的任务添加一个类似于监听的东西,用来处理对应的消息
        Futures.addCallback(listenableFuture, new FutureCallback<Boolean>() {
            public void onSuccess(Boolean result) {
            	//ctx这个处理通道此时就比较重要的,他保证了一一对应关系,使用我们自定义的请求结构和返回结构来进行对应的消息处理
                ctx.writeAndFlush(response).addListener(new ChannelFutureListener() {
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        System.out.println("RPC Server Send message-id respone:" + request.getMessageId());
                    }
                });
            }

            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        }, threadPoolExecutor);
    }

    /**
     * 通过spring容器启动之后,得到容器中的信息,ApplicationContext
     */
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        try {
        	//获取rpc服务映射容器
            MessageKeyVal keyVal = (MessageKeyVal) ctx.getBean(Class.forName("com.rpc.model.MessageKeyVal"));
            //获取map容器,因为对应的配置文件xml中已经配置了初始化数据即:
           /* <bean id="rpcbean" class="com.rpc.model.MessageKeyVal">
            <property name="messageKeyVal">
              <map>
                <entry key="com.rpc.servicebean.Calculate">
                  <ref bean="calc"/>
                </entry>
              </map>
            </property>
          </bean>*/
            //所以在这里,可以获取到对应的map数据信息
            Map<String, Object> rpcServiceObject = keyVal.getMessageKeyVal();

            Set s = rpcServiceObject.entrySet();
            Iterator<Map.Entry<String, Object>> it = s.iterator();
            Map.Entry<String, Object> entry;

            while (it.hasNext()) {
                entry = it.next();
                //把xml中配置的map数据信息放入concurrentHashMap中
                handlerMap.put(entry.getKey(), entry.getValue());
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MessageRecvExecutor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void afterPropertiesSet() throws Exception {
    	
        ThreadFactory threadRpcFactory = new NamedThreadFactory("NettyRPC ThreadFactory");

        //获取可用的进行数量,也就是显示java虚拟机可用的处理器个数
        int parallel = Runtime.getRuntime().availableProcessors() * 2;

        //EventLoopGroup继承自EventExecutorGroup接口
        //EventExecutorGroup是直接继承ScheduledExecutorService这个接口的，
        //ScheduledExecutorService的用:主要就是可以在一段延迟之后或者每隔一段时间执行task的ExecutorService定义
        //是用来实现NIO多路复用的，
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup(parallel, threadRpcFactory, SelectorProvider.provider());

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            //NioServerSocketChannel简介:
            //io.netty.channel.socket.ServerSocketChannel} implementation which uses
            //* NIO selector based implementation to accept new connections.
            bootstrap.group(boss, worker).channel(NioServerSocketChannel.class)
            		//同时初始化消息接收器吧
                    .childHandler(new MessageRecvChannelInitializer(handlerMap).buildRpcSerializeProtocol(serializeProtocol))
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);//检测死链接

            String[] ipAddr = serverAddress.split(MessageRecvExecutor.DELIMITER);

            if (ipAddr.length == 2) {
                String host = ipAddr[0];
                int port = Integer.parseInt(ipAddr[1]);
                ChannelFuture future = bootstrap.bind(host, port).sync();
                System.out.printf("[author tangjie] Netty RPC Server start success!\nip:%s\nport:%d\nprotocol:%s\n\n", host, port, serializeProtocol);
                future.channel().closeFuture().sync();
            } else {
                System.out.printf("[author tangjie] Netty RPC Server start fail!\n");
            }
        } finally {
            worker.shutdownGracefully();
            boss.shutdownGracefully();
        }
    }
}
