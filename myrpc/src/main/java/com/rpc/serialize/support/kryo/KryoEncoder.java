/**
 * @filename:KryoEncoder.java
 *
 * Newland Co. Ltd. All rights reserved.
 *
 * @Description:Kryo编码器
 * @author tangjie
 * @version 1.0
 *
 */
package com.rpc.serialize.support.kryo;

import com.rpc.serialize.support.MessageCodecUtil;
import com.rpc.serialize.support.MessageEncoder;

public class KryoEncoder extends MessageEncoder {

    public KryoEncoder(MessageCodecUtil util) {
        super(util);
    }
}
