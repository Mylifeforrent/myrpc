/**
 * @filename:HessianEncoder.java
 *
 * Newland Co. Ltd. All rights reserved.
 *
 * @Description:Hessian编码器
 * @author tangjie
 * @version 1.0
 *
 */
package com.rpc.serialize.support.hessian;

import com.rpc.serialize.support.MessageCodecUtil;
import com.rpc.serialize.support.MessageEncoder;

public class HessianEncoder extends MessageEncoder {

    public HessianEncoder(MessageCodecUtil util) {
        super(util);
    }
}
