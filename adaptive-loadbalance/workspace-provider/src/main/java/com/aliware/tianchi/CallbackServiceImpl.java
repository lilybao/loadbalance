package com.aliware.tianchi;

import org.apache.dubbo.rpc.listener.CallbackListener;
import org.apache.dubbo.rpc.service.CallbackService;

import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author daofeng.xjf
 * <p>
 * 服务端回调服务
 * 可选接口
 * 用户可以基于此服务，实现服务端向客户端动态推送的功能
 */
public class CallbackServiceImpl implements CallbackService {

//    private static String SMALL_NAME = "small";
//    private static String MEDIUM_NAME = "";
//    private static String LARGE_NAME;
//
//    public static volatile AtomicInteger count = new AtomicInteger();
//
//    public static volatile int max_concurrent_thread;
//
//    static {
//        String quota = System.getProperty("quota");
//        if (quota.equals("small")) {
//            max_concurrent_thread = (int) (200 * 1.1);
//        }
//        if (quota.equals("medium")) {
//            max_concurrent_thread = (int) (450 * 1.1);
//        }
//        if (quota.equals("large")) {
//            max_concurrent_thread = (int) (650 * 1.1);
//        }
//    }

    public CallbackServiceImpl() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!listeners.isEmpty()) {
                    for (Map.Entry<String, CallbackListener> entry : listeners.entrySet()) {
                        try {
//                            entry.getValue().receiveServerMsg(System.getProperty("quota") + "-" + (max_concurrent_thread - count.get()));
                            entry.getValue().receiveServerMsg(System.getProperty("quota"));
                        } catch (Throwable t1) {
                            listeners.remove(entry.getKey());
                        }
                    }
                }
            }
        }, 0, 5000);
    }

    private Timer timer = new Timer();

    /**
     * key: listener type
     * value: callback listener
     */
    private final Map<String, CallbackListener> listeners = new ConcurrentHashMap<>();

    @Override
    public void addListener(String key, CallbackListener listener) {
        listeners.put(key, listener);
        listener.receiveServerMsg(new Date().toString()); // send notification for change
    }
}
