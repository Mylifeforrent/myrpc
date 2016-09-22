/**
 * @filename:RpcRecvSerializeFrame.java
 *
 * Newland Co. Ltd. All rights reserved.
 *
 * @Description:RPC服务端消息序列化协议框架
 * @author tangjie
 * @version 1.0
 *
 */
package com.rpc.core;

import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.util.Map;

import com.rpc.serialize.support.MessageCodecUtil;
import com.rpc.serialize.support.RpcSerializeFrame;
import com.rpc.serialize.support.RpcSerializeProtocol;
import com.rpc.serialize.support.hessian.HessianCodecUtil;
import com.rpc.serialize.support.hessian.HessianDecoder;
import com.rpc.serialize.support.hessian.HessianEncoder;
import com.rpc.serialize.support.kryo.KryoCodecUtil;
import com.rpc.serialize.support.kryo.KryoDecoder;
import com.rpc.serialize.support.kryo.KryoEncoder;
import com.rpc.serialize.support.kryo.KryoPoolFactory;

/**
 * 自定义rpc序列化框架
 * @author PanTian
 *
 */
public class RpcRecvSerializeFrame implements RpcSerializeFrame {

	//用于接收传入的map参数
    private Map<String, Object> handlerMap = null;

    public RpcRecvSerializeFrame(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    /**
     * 根据序列化方式来选择使用那些编码解码方式
     * 对传入的数据进行解码操作,然后处理请求
     */
    public void select(RpcSerializeProtocol protocol, ChannelPipeline pipeline) {
        switch (protocol) {
            case JDKSERIALIZE: {
                pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, MessageCodecUtil.MESSAGE_LENGTH, 0, MessageCodecUtil.MESSAGE_LENGTH));
                pipeline.addLast(new LengthFieldPrepender(MessageCodecUtil.MESSAGE_LENGTH));
                pipeline.addLast(new ObjectEncoder());
                pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
                //传入处理的handler类
                pipeline.addLast(new MessageRecvHandler(handlerMap));
                break;
            }
            case KRYOSERIALIZE: {
                KryoCodecUtil util = new KryoCodecUtil(KryoPoolFactory.getKryoPoolInstance());
                pipeline.addLast(new KryoEncoder(util));
                pipeline.addLast(new KryoDecoder(util));
                pipeline.addLast(new MessageRecvHandler(handlerMap));
                break;
            }
            case HESSIANSERIALIZE: {
                HessianCodecUtil util = new HessianCodecUtil();
                pipeline.addLast(new HessianEncoder(util));
                pipeline.addLast(new HessianDecoder(util));
                pipeline.addLast(new MessageRecvHandler(handlerMap));
                break;
            }
        }
    }
}

