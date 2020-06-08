package com.neoniou.swu.schedule;

import com.neoniou.swu.util.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Neo.Zzj
 */
@Component
public class KeepAlive {

    /*private static final Logger logger = LoggerFactory.getLogger(KeepAlive.class);

    *//**
     * 每十分钟发送一次请求
     * 测试连接状态
     *//*
    @Scheduled(cron = "0 0,10,20,30,40,50 * * * ?")
    public void keepConnectAlive() {
        HttpClientUtil httpClientUtil = new HttpClientUtil();
        int statusCode = httpClientUtil.testConnect();
        logger.info("Test connect, status code: {}", statusCode);
    }*/
}
