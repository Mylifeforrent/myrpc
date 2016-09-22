/**
 * @filename:RpcServerKryoProtocolStarter.java
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

public class RpcServerKryoProtocolStarter {

    @SuppressWarnings("resource")
	public static void main(String[] args) {
    	//System.out.println("*********打印出classpath********:  "+System.getProperty("java.class.path"));
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("rpc-invoke-config-kryo.xml");
    }
}

