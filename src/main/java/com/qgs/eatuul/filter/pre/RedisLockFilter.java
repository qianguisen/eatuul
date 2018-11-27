package com.qgs.eatuul.filter.pre;

import com.qgs.eatuul.constants.FilterType;
import com.qgs.eatuul.filter.EatuulFilter;
import com.qgs.eatuul.log.Log;
import com.qgs.eatuul.utils.RedisTool;
import jdk.nashorn.internal.scripts.JD;
import org.slf4j.Logger;
import redis.clients.jedis.Jedis;

import java.util.UUID;

/**
 * @author qianguisen
 * 2018/11/8 16:07
 * @Description: TODO
 */
public class RedisLockFilter extends EatuulFilter {

    @Log
    Logger log;

    @Override
    public String filterType() {
        return FilterType.PRE;
    }

    @Override
    public int filterOrder() {
        return -2;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        Jedis jedis = new Jedis("127.0.0.1"); // 默认端口
        String Uid = UUID.randomUUID().toString();
        for(;;){
            if(RedisTool.tryGetDistributedLock(jedis,"aaaa", Uid,60000)){
                log.info("===========redis lock...==========");
                break;
            }
        }


        RedisTool.releaseDistributedLock(jedis, "aaaa", Uid);
        return null;
    }

}
