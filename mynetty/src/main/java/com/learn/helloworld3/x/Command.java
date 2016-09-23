package com.learn.helloworld3.x;

import java.io.Serializable;

/**
 * 自定义简单的pojo用来进行netty通信数据的封装
 * @author PanTian
 *
 */
public class Command implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2872005424706695978L;
	
	private String actionName;

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	
	

}
