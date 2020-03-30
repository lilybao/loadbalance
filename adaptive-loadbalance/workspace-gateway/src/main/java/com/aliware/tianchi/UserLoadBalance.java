package com.aliware.tianchi;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.cluster.LoadBalance;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author daofeng.xjf
 * <p>
 * 负载均衡扩展接口
 * 必选接口，核心接口
 * 此类可以修改实现，不可以移动类或者修改包名
 * 选手需要基于此类实现自己的负载均衡算法
 */
public class UserLoadBalance implements LoadBalance {

    //三个端口
    private static final Integer SMALL_PORT = 20880;
    private static final Integer MEDIUM_PORT = 20870;
    private static final Integer LARGE_PORT = 20890;

    //三个invoker
    public static volatile Invoker SMALL_INVOKER = null;
    public static volatile Invoker MEDIUM_INVOKER = null;
    public static volatile Invoker LARGE_INVOKER = null;

    public static AtomicBoolean small_flag = new AtomicBoolean(false);
    public static AtomicBoolean medium_flag = new AtomicBoolean(false);
    public static AtomicBoolean large_flag = new AtomicBoolean(false);


    //三个provider的线程池容量
    public static AtomicInteger SMALL_THREAD = new AtomicInteger(200);
    public static AtomicInteger MEDIUM_THREAD = new AtomicInteger(450);
    public static AtomicInteger LARGE_THREAD = new AtomicInteger(650);

    public static ReentrantLock SMALL_LOCK = new ReentrantLock();
    public static ReentrantLock MEDIUM_LOCK = new ReentrantLock();
    public static ReentrantLock LARGE_LOCK = new ReentrantLock();

    @Override
    public <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation) throws RpcException {
        initInvoker(invokers);
        int i = ThreadLocalRandom.current().nextInt(SMALL_THREAD.get() + MEDIUM_THREAD.get() + LARGE_THREAD.get());
        if (i > MEDIUM_THREAD.get()) {
            if (!large_flag.get()) {
                return LARGE_INVOKER;
            } else if (!medium_flag.get()) {
                return MEDIUM_INVOKER;
            } else if (!small_flag.get()) {
                return SMALL_INVOKER;
            }
        }
        if (i > SMALL_THREAD.get() && i < MEDIUM_THREAD.get()) {
            if (!medium_flag.get()) {
                return MEDIUM_INVOKER;
            } else if (large_flag.get()) {
                return LARGE_INVOKER;
            } else if (!small_flag.get()) {
                return SMALL_INVOKER;
            }
        }
        if (i < SMALL_THREAD.get()) {
            if (!small_flag.get()) {
                return SMALL_INVOKER;
            } else if (!medium_flag.get()) {
                return MEDIUM_INVOKER;
            } else if (!large_flag.get()) {
                return LARGE_INVOKER;
            }
        }
        return invokers.get(ThreadLocalRandom.current().nextInt(invokers.size()));
    }

    private <T> void initInvoker(List<Invoker<T>> invokers) {
        if (null == SMALL_INVOKER) {
            try {
                if (SMALL_LOCK.tryLock()) {
                    if (null == SMALL_INVOKER) {
                        for (int i = 0; i < invokers.size(); i++) {
                            if (invokers.get(i).getUrl().getPort() == SMALL_PORT) {
                                SMALL_INVOKER = invokers.get(i);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (SMALL_LOCK.isLocked()) {
                    SMALL_LOCK.unlock();
                }
            }
        }
        if (null == MEDIUM_INVOKER) {
            try {
                if (MEDIUM_LOCK.tryLock()) {
                    if (null == MEDIUM_INVOKER) {
                        for (int i = 0; i < invokers.size(); i++) {
                            if (invokers.get(i).getUrl().getPort() == MEDIUM_PORT) {
                                MEDIUM_INVOKER = invokers.get(i);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (MEDIUM_LOCK.isLocked()) {
                    MEDIUM_LOCK.unlock();
                }

            }
        }
        if (null == LARGE_INVOKER) {
            try {
                if (LARGE_LOCK.tryLock()) {
                    if (null == LARGE_INVOKER) {
                        for (int i = 0; i < invokers.size(); i++) {
                            if (invokers.get(i).getUrl().getPort() == LARGE_PORT) {
                                LARGE_INVOKER = invokers.get(i);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (LARGE_LOCK.isLocked()) {
                    LARGE_LOCK.unlock();
                }

            }
        }
    }
}

