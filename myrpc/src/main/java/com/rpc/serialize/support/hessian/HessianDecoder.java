/**
 * @filename:HessianDecoder.java
 *
 * Newland Co. Ltd. All rights reserved.
 *
 * @Description:Hessian解码器
 * @author tangjie
 * @version 1.0
 *
 */
package com.rpc.serialize.support.hessian;

import com.rpc.serialize.support.MessageCodecUtil;
import com.rpc.serialize.support.MessageDecoder;

public class HessianDecoder extends MessageDecoder {

    public HessianDecoder(MessageCodecUtil util) {
        super(util);
    }
}
