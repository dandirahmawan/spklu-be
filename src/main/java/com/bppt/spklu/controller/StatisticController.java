package com.bppt.spklu.controller;

import com.bppt.spklu.model.ReqStatistic;
import com.bppt.spklu.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/statistic")
public class StatisticController extends CommonController {

    @Override
    protected boolean isSecure() {
        return false;
    }

    @Autowired
    private StatisticService statisticService;

    @GetMapping
    public ResponseEntity getToday(HttpServletRequest req, HttpServletResponse res) {
        return res(req, res, null, statisticService::getToday);
    }

    @PostMapping
    public ResponseEntity getBySpecific(HttpServletRequest req, HttpServletResponse res, @RequestBody ReqStatistic reqStatistic,
                                        @Param("userType") Integer userType) {
        return res(req, res, null, () -> statisticService.getBySpecific(reqStatistic, userType));
    }
}
