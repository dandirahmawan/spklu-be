package com.bppt.spklu.controller;

import com.bppt.spklu.config.ResErrExc;
import com.bppt.spklu.entity.TranTracking;
import com.bppt.spklu.model.ResponseRest;
import com.bppt.spklu.service.TranTrackingService;
import com.bppt.spklu.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public abstract class CommonController {

    protected abstract boolean isSecure();

    @Autowired
    private TranTrackingService tranTrackingService;

    @Autowired
    private UserService userService;

    protected <T> ResponseEntity res(HttpServletRequest req, HttpServletResponse res, Object body, SupplierRes<T> func) {
        String ipAddress = req.getRemoteHost();
        String uri = req.getServletPath();
        String jsonBody = "";
        String user = "";

        if (body != null) {
            try {
                ObjectWriter ow = new ObjectMapper().writer(); //.withDefaultPrettyPrinter();
                jsonBody = ow.writeValueAsString(body);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        try {
            TranTracking t = new TranTracking();
            t.setCreateDate(new Date());
            t.setIpAddress(ipAddress);
            t.setRequestBody(jsonBody);
            t.setRequestUri(uri);
            t.setUser(user);
            tranTrackingService.save(t);
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean isSecure = req.getAttribute("isSecure") != null
                && req.getAttribute("isSecure").toString().equalsIgnoreCase("false") ?
                false : true;

        if (isSecure()) {
            String token = req.getHeader("token");
            if (!isSecure || (token != null && userService.validToken(token))) return surroundProcess(func);
            else {
                ResponseRest<T> r = new ResponseRest<T>("invalid token", false, null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(r);
            }
        } else {
            return surroundProcess(func);
        }
    }

    private <T> ResponseEntity surroundProcess(SupplierRes<T> func) {
        T t;
        ResponseRest<T> r;

        try {
            t = func.get();
            r = new ResponseRest<T>("success", true, t);
            return ResponseEntity.ok().body(r);
        } catch (ResErrExc resErrExc) {
            r = new ResponseRest<T>(resErrExc.getMsg(), false, null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(r);
        }
    }

    @FunctionalInterface
    public interface SupplierRes<T> {
        T get() throws ResErrExc;
    }

}
