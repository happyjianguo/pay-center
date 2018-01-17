package com.dream.pay.center.service.sequence;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 生成唯一单号
 *
 * @author mengzhenbin
 */
public class KeyUtils {

    /**
     * 生成18位的纳秒流水号,旧表使用(旧表流水号为bigInt(20))
     * 单线程以及多线程都测试过,暂无冲突
     *
     * @return 18-n位纳秒数+n位随机数,n大于等于1
     */
    public static long genNanoNo() {
        int length = 18;
        long nanoTime = System.nanoTime();
        String nanoTimeStr = String.valueOf(nanoTime);
        if (nanoTimeStr.length() >= length) {
            return Long.parseLong(nanoTimeStr.substring(nanoTimeStr.length() - length, nanoTimeStr.length()));

        }

        int offSet = (int) Math.pow(10, length - nanoTimeStr.length());
        return nanoTime * offSet + (long) ThreadLocalRandom
                .current().nextInt(0, offSet);
    }

}
