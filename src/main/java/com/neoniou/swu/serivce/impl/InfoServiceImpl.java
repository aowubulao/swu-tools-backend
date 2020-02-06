package com.neoniou.swu.serivce.impl;

import com.neoniou.swu.exception.ExceptionEnum;
import com.neoniou.swu.exception.NeoException;
import com.neoniou.swu.serivce.InfoService;
import com.neoniou.swu.util.HttpClientUtil;
import com.neoniou.swu.util.LoginUtil;
import com.neoniou.swu.util.JsonUtil;
import org.springframework.stereotype.Service;

/**
 * @author Neo.Zzj
 */
@Service
public class InfoServiceImpl implements InfoService {

    private static final String NETWORK_ERROR = "NETWORK_ERROR";
    private static final String LOGIN_FAIL = "LOGIN_FAIL";

    @Override
    public Object getGrades(String username, String password, String xnm, String xqm) {
        String cookie =  getCookie(username, password);

        HttpClientUtil httpClientUtil = new HttpClientUtil();
        String grades = httpClientUtil.getGrades(cookie, xnm, xqm);

        return JsonUtil.parseGrades(grades);
    }

    @Override
    public Object getCourses(String username, String password, String xnm, String xqm) {
        String cookie =  getCookie(username, password);

        HttpClientUtil httpClientUtil = new HttpClientUtil();
        String courses = httpClientUtil.getCourses(cookie, xnm, xqm);

        return JsonUtil.parseCourses(courses);
    }

    @Override
    public String getUtility(String buildId, String roomCode) {
        HttpClientUtil httpClientUtil = new HttpClientUtil();
        return httpClientUtil.getUtility(buildId, roomCode);
    }

    /**
     * 登录获取 Cookie
     * @param username
     * @param password
     * @return
     */
    private String getCookie(String username, String password) {
        LoginUtil loginUtil = new LoginUtil(username, password);
        String cookie = loginUtil.login();
        checkException(cookie);
        return cookie;
    }

    /**
     * 根据 Cookie返回错误信息
     * @param cookie
     */
    private void checkException(String cookie) {
        if (LOGIN_FAIL.equals(cookie)) {
            throw new NeoException(ExceptionEnum.LOGIN_FAIL);
        }
        if (NETWORK_ERROR.equals(cookie)) {
            throw new NeoException(ExceptionEnum.NETWORK_ERROR);
        }
    }
}
