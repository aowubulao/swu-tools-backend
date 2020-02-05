package com.neoniou.swu.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.neoniou.swu.pojo.Course;
import com.neoniou.swu.pojo.Grade;

import java.util.List;

/**
 * @author Neo.Zzj
 */
public class JsonUtil {

    /**
     * 转换成绩
     * @param grades
     * @return
     */
    public static Object parseGrades(String grades) {

        String items = JSON.parseObject(grades).get("items").toString();

        List<Grade> grades1 = JSON.parseObject(items, new TypeReference<List<Grade>>() {});

        return JSON.toJSON(grades1);
    }

    /**
     * 转换课表
     * @param courses
     * @return
     */
    public static Object parseCourses(String courses) {

        String kbList = JSON.parseObject(courses).get("kbList").toString();

        List<Course> courses1 = JSON.parseObject(kbList, new TypeReference<List<Course>>() {});

        return JSON.toJSON(courses1);
    }
}
