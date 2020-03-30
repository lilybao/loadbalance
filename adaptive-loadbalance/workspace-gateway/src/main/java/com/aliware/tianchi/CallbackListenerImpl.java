package com.aliware.tianchi;

import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.rpc.listener.CallbackListener;

/**
 * @author daofeng.xjf
 * <p>
 * 客户端监听器
 * 可选接口
 * 用户可以基于获取获取服务端的推送信息，与 CallbackService 搭配使用
 */
public class CallbackListenerImpl implements CallbackListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static volatile int small_active_count;
    private static volatile int medium_active_count;
    private static volatile int large_active_count;

    @Override
    public void receiveServerMsg(String msg) {
//        if (msg.split("-").length > 0) {
//            if (msg.split("-")[0].equals("small")) {
//                UserLoadBalance.SMALL_THREAD.getAndAdd(Integer.valueOf(msg.split("-")[1]));
//                logger.info("SMALL_THREAD" + UserLoadBalance.SMALL_THREAD.get());
//            }
//            if (msg.split("-")[0].equals("medium")) {
//                UserLoadBalance.MEDIUM_THREAD.getAndAdd(Integer.valueOf(msg.split("-")[1]));
//                logger.info("MEDIUM_THREAD" + UserLoadBalance.MEDIUM_THREAD.get());
//            }
//            if (msg.split("-")[0].equals("large")) {
//                UserLoadBalance.LARGE_THREAD.getAndAdd(Integer.valueOf(msg.split("-")[1]));
//                logger.info("LARGE_THREAD" + UserLoadBalance.LARGE_THREAD.get());
//            }
//        }
        logger.info("receive msg from server :" + msg);
    }

}
