package com.aliware.tianchi;

import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author daofeng.xjf
 * <p>
 * 客户端过滤器
 * 可选接口
 * 用户可以在客户端拦截请求和响应,捕获 rpc 调用时产生、服务端返回的已知异常。
 */
@Activate(group = Constants.CONSUMER)
public class TestClientFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try {
            Result result = invoker.invoke(invocation);
            return result;
        } catch (Exception e) {
            //当某个provider发生阻塞后 ，暂停发送50ms
            if (invoker.equals(UserLoadBalance.SMALL_INVOKER)) {
                if (UserLoadBalance.small_flag.compareAndSet(false, true)) {
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    UserLoadBalance.small_flag.set(false);
                }
            }
            if (invoker.equals(UserLoadBalance.LARGE_INVOKER)) {
                if (UserLoadBalance.large_flag.compareAndSet(false, true)) {
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    UserLoadBalance.large_flag.set(false);
                }
            }
            if (invoker.equals(UserLoadBalance.MEDIUM_INVOKER)) {
                if (UserLoadBalance.medium_flag.compareAndSet(false, true)) {
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    UserLoadBalance.medium_flag.set(false);
                }
            }
            throw e;
        }

    }

    @Override
    public Result onResponse(Result result, Invoker<?> invoker, Invocation invocation) {
        return result;
    }
}
