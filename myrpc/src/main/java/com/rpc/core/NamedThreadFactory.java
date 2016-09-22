/**
 * @filename:NamedThreadFactory.java
 *
 * Newland Co. Ltd. All rights reserved.
 *
 * @Description:线程工厂
 * @author tangjie
 * @version 1.0
 *
 */
package com.rpc.core;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadFactory implements ThreadFactory {

    private static final AtomicInteger threadNumber = new AtomicInteger(1);

    //初始值为1,用于后续的自增长,来进行命名后缀的递增,定义为final表示不可更改的,只可以使用对象方法进行改动
    private final AtomicInteger mThreadNum = new AtomicInteger(1);

    private final String prefix;

    private final boolean daemoThread;

    private final ThreadGroup threadGroup;

    public NamedThreadFactory() {
        this("rpcserver-threadpool-" + threadNumber.getAndIncrement(), false);
    }

    public NamedThreadFactory(String prefix) {
        this(prefix, false);
    }

    //通过构造函数获取当先前程数据哪个线程组
    public NamedThreadFactory(String prefix, boolean daemo) {
        this.prefix = prefix + "-thread-";//前缀
        daemoThread = daemo;//
        SecurityManager s = System.getSecurityManager();
        threadGroup = (s == null) ? Thread.currentThread().getThreadGroup() : s.getThreadGroup();
    }

/*    在Java中有两类线程：User Thread(用户线程)、Daemon Thread(守护线程)       
    Daemon的作用是为其他线程的运行提供便利服务，比如垃圾回收线程就是一个很称职的守护者。
    User和Daemon两者几乎没有区别，唯一的不同之处就在于虚拟机的离开：如果 User Thread已经全部退出运行了，
	 只剩下Daemon Thread存在了，虚拟机也就退出了。
	 因为没有了被守护者，Daemon也就没有工作可做了，也就没有继续运行程序的必要了。       
          值得一提的是，守护线程并非只有虚拟机内部提供，用户在编写程序时也可以自己设置守护线程。*/
    
    /**
     * 获取一个新的线程
     */
    public Thread newThread(Runnable runnable) {
        String name = prefix + mThreadNum.getAndIncrement();//获取后缀递增的线程名
        //新建一个线程,传入当先线程所属的线程组,名字等,这个stackSize参数为0 表示此处stackSize参数可以忽略
        Thread ret = new Thread(threadGroup, runnable, name, 0);
        //设置该线程是否为守护线程,这个大概就是rpc服务端运行的一个注意点吧? 把服务端运行任务放在守护线程里运行
        ret.setDaemon(daemoThread);
        return ret;
    }

    public ThreadGroup getThreadGroup() {
        return threadGroup;
    }
}
