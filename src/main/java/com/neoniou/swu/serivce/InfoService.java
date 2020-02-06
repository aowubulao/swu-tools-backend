package com.neoniou.swu.serivce;

import com.neoniou.swu.pojo.Grade;

import java.util.List;

/**
 * @author Neo.Zzj
 */
public interface InfoService {

    /**
     * 获取成绩
     * @param username
     * @param password
     * @param xnm 学年
     * @param xqm 学期
     * @return
     */
    Object getGrades(String username, String password, String xnm, String xqm);

    /**
     * 获取课程表
     * @param username
     * @param password
     * @param xnm
     * @param xqm
     * @return
     */
    Object getCourses(String username, String password, String xnm, String xqm);

    /**
     * 获取水电费余额
     * @param buildId
     * @param roomCode
     * @return
     */
    String getUtility(String buildId, String roomCode);
}
