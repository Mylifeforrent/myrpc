/**
 * @filename:RpcServerHessianProtocolStarter.java
 *
 * Newland Co. Ltd. All rights reserved.
 *
 * @Description:rpc服务器启动模块
 * @author tangjie
 * @version 1.0
 *
 */
package com.rpc.core;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RpcServerHessianProtocolStarter {

    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("rpc-invoke-config-hessian.xml");
    }
}

