package com.dream.pay.center.service.sequence;

import com.youzan.platform.cache.CacheTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by mengzhenbin on 15/12/16.
 */

@Slf4j
@Component
public class SerialidGenerator {

    @Resource
    private CacheTemplate serialCacheTemplate;

    public String get() {
//        try {
//            return String.valueOf(serialCacheTemplate.serialid());
//        } catch (Exception e) {
//            log.error("发号器生成流水号异常", e);
//        }
        return String.valueOf(KeyUtils.genNanoNo());
    }
}
