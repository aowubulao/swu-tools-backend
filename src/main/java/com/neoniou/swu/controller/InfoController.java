package com.neoniou.swu.controller;

import com.neoniou.swu.serivce.InfoService;
import com.neoniou.swu.util.XqmUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(InfoController.class);

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
        logger.info("user: [{}] get grades", username);
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
        logger.info("user: [{}] get courses", username);
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
        logger.info("buildId: [{}], roomCode: [{}] query balance", buildId, roomCode);
        if (roomCode.length() != 4) {
            roomCode = "0" + roomCode;
        }
        return ResponseEntity.ok().body(infoService.getUtility(buildId, roomCode));
    }

    @GetMapping("/hello")
    public ResponseEntity<String> test() {
        logger.info("Hello has been used");
        return ResponseEntity.ok().body("hello!");
    }
}
