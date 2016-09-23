package com.learn.helloworld3.x;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 * 可以发现,具体的执行都是交给了handler来执行
 * @author PanTian
 *
 */
public class HelloClientHandler extends SimpleChannelHandler {  


    /** 
     * 当绑定到服务端的时候触发，打印"Hello world, I'm client." 
     *  
     */
    @Override  
    public void channelConnected(ChannelHandlerContext ctx,  
            ChannelStateEvent e) {  
        System.out.println("Hello world, I'm client.");  
    }  
}  
