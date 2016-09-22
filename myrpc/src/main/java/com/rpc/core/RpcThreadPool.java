/**
 * @filename:RpcThreadPool.java
 *
 * Newland Co. Ltd. All rights reserved.
 *
 * @Description:rpc线程池封装
 * @author tangjie
 * @version 1.0
 *
 */
package com.rpc.core;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 自己定义的一个RPC线程池
 * @author PanTian
 *
 */
public class RpcThreadPool {
    public static Executor getExecutor(int threads, int queues) {
        String name = "RpcThreadPool";
        //如果队列的序列号是0那么久新生成1个同步化的线程队列 SynchronousQueue<Runnable>(),
        //如果<0那么久生成1个队列形式的线程 LinkedBlockingQueue<Runnable>()
        //如果都不满足,那就生成对应数量的队列形式的线程
        return new ThreadPoolExecutor(threads, threads, 0, TimeUnit.MILLISECONDS,
                queues == 0 ? new SynchronousQueue<Runnable>()
                        : (queues < 0 ? new LinkedBlockingQueue<Runnable>()
                                : new LinkedBlockingQueue<Runnable>(queues)),
                new NamedThreadFactory(name, true), new AbortPolicyWithReport(name));
    }
}
