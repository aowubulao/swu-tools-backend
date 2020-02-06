package com.neoniou.swu.controller;

import com.neoniou.swu.serivce.InfoService;
import com.neoniou.swu.util.XqmUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Neo.Zzj
 */
@RestController
@RequestMapping("/swu/info")
public class InfoController {

    @Autowired
    private InfoService infoService;

    /**
     * 获取成绩
     * @param username
     * @param password
     * @param xnm 学年
     * @param xqm 学期
     * @return
     */
    @GetMapping("/grades")
    public ResponseEntity<Object> getGrades(@RequestParam("username") String username,
                                            @RequestParam("password") String password,
                                            @RequestParam("xnm") String xnm,
                                            @RequestParam("xqm") String xqm) {
        // Exchange
        xqm = XqmUtil.exchange(xqm);

        return ResponseEntity.ok().body(infoService.getGrades(username, password, xnm, xqm));
    }

    /**
     * 获取课表
     * @param username
     * @param password
     * @param xnm
     * @param xqm
     * @return
     */
    @GetMapping("/courses")
    public ResponseEntity<Object> getCourses(@RequestParam("username") String username,
                                             @RequestParam("password") String password,
                                             @RequestParam("xnm") String xnm,
                                             @RequestParam("xqm") String xqm) {
        // Exchange
        xqm = XqmUtil.exchange(xqm);

        return ResponseEntity.ok().body(infoService.getCourses(username, password, xnm, xqm));
    }

    /**
     * 获取水电费余额
     * @param buildId
     * @param roomCode
     * @return
     */
    @GetMapping("/utility")
    public ResponseEntity<String> getUtility(@RequestParam("buildId") String buildId,
                                             @RequestParam("roomCode") String roomCode) {
        if (roomCode.length() != 4) {
            roomCode = "0" + roomCode;
        }
        return ResponseEntity.ok().body(infoService.getUtility(buildId, roomCode));
    }

    @GetMapping("/hello")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok().body("hello!");
    }
}
