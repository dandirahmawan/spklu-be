package com.bppt.spklu.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/visit")
public class VisitController extends CommonController {

    @Override
    protected boolean isSecure() {
        return false;
    }

    @GetMapping
    public ResponseEntity visit(HttpServletRequest req, HttpServletResponse res) {
        return res(req, res, null, () -> null);
    }

}
