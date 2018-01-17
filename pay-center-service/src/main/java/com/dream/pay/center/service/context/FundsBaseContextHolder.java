package com.dream.pay.center.service.context;

/**
 * 收单上下文Holder
 *
 * Created by mengzhenbin on 16/7/21.
 */

public class FundsBaseContextHolder {

    /** 本地线程 */
    private static ThreadLocal<FundsBaseContext> contextLocal = new ThreadLocal<FundsBaseContext>();
    /**
     * 获取收单上下文对象
     *
     * @return 返回收单上下文对象
     */
    public static FundsBaseContext get() {
        return contextLocal.get();
    }

    /**
     * 设置收单上下文
     *
     * @param context 收单上下文信息
     */
    public static void set(FundsBaseContext context) {
        contextLocal.set(context);
    }

    /**
     * 清除缓存
     */
    public static void clear() {
        contextLocal.set(null);
    }
}
