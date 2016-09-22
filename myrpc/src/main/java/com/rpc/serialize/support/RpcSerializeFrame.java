/**
 * @filename:RpcSerializeFrame.java
 *
 * Newland Co. Ltd. All rights reserved.
 *
 * @Description:RPC消息序序列化协议选择器接口
 * @author tangjie
 * @version 1.0
 *
 */
package com.rpc.serialize.support;

import io.netty.channel.ChannelPipeline;

public interface RpcSerializeFrame {

	/**
	 * Netty4
		Netty是一个和MINA类似的Java NIO框架，目前的最新版本是4.0.13，这两个框架的主要作者好像都是同一个韩国人。
		
		Channel
		Channel是Netty最核心的接口，一个Channel就是一个联络Socket的通道，通过Channel，你可以对Socket进行各种操作。
		
		ChannelHandler
		用Netty编写网络程序的时候，你很少直接操纵Channel，而是通过ChannelHandler来间接操纵Channel。
		
		ChannelPipeline
		ChannelPipeline实际上应该叫做ChannelHandlerPipeline，可以把ChannelPipeline看成是一个ChandlerHandler的链表，
		当需要对Channel进行某种处理的时候，Pipeline负责依次调用每一个Handler进行处理。每个Channel都有一个属于自己的Pipeline，
		调用Channel#pipeline()方法可以获得Channel的Pipeline，调用Pipeline#channel()方法可以获得Pipeline的Channel。
	 * @param protocol
	 * @param pipeline
	 */
    public void select(RpcSerializeProtocol protocol, ChannelPipeline pipeline);
}
