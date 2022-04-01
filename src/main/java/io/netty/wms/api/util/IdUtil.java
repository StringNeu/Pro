package io.netty.wms.api.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * ID生成类
 */
public final class IdUtil {

    //原子操作
    private static final AtomicLong IDX = new AtomicLong();

    public static long nextId() {
        return IDX.incrementAndGet();
    }

    private IdUtil() {
        //no instance
    }
}