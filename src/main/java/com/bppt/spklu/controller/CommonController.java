package com.bppt.spklu.controller;

import com.bppt.spklu.config.ResErrExc;
import com.bppt.spklu.entity.TranTracking;
import com.bppt.spklu.entity.UserType;
import com.bppt.spklu.model.ResponseRest;
import com.bppt.spklu.service.TranTrackingService;
import com.bppt.spklu.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Slf4j
public abstract class CommonController {

    protected abstract boolean isSecure();

    @Autowired
    private TranTrackingService tranTrackingService;

    @Autowired
    private UserService userService;

    protected <T> ResponseEntity res(HttpServletRequest req, HttpServletResponse res, Object body, SupplierRes<T> func) {
        String ipAddress = req.getHeader("X-Forwarded-For"); //req.getRemoteHost();

        String queryParam = "";
        if (req.getQueryString() != null) queryParam = "?" + req.getQueryString();
        String uri = req.getServletPath() + queryParam;

        String jsonBody = null;
        String token = req.getHeader("token");
        String userType = req.getHeader("user-type");

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

            if (ipAddress == null || ipAddress.isEmpty()) t.setIpAddress(req.getRemoteHost());
            else t.setIpAddress(ipAddress);

            t.setRequestUri(uri);

            if (jsonBody != null) t.setRequestBody(jsonBody);
            String user;
            if (token != null && userService.validToken(token)) {
                user = userService.getUsernameByToken(token);
                t.setUser(user);
            }
            if (userType != null && !userType.isEmpty()) t.setUserType(new UserType(Integer.parseInt(userType)));

            tranTrackingService.save(t);
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean isSecure = req.getAttribute("isSecure") != null
                && req.getAttribute("isSecure").toString().equalsIgnoreCase("false") ?
                false : true;
        boolean isWithoutUserType = req.getAttribute("isWithoutUserType") != null
                && req.getAttribute("isWithoutUserType").toString().equalsIgnoreCase("false") ?
                false : true;

        if (isSecure()) {
            if (!isSecure) {
                if (!isWithoutUserType) return surroundProcess(func);
                else {
                    if (userType != null) return surroundProcess(func);
                }
            } else {
                if (token != null && userService.validToken(token)) return surroundProcess(func);
            }
            return unauthorized();
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

    private <T> ResponseEntity unauthorized() {
        ResponseRest<T> r = new ResponseRest<T>("invalid token or user type", false, null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(r);
    }

    @FunctionalInterface
    public interface SupplierRes<T> {
        T get() throws ResErrExc;
    }

}
