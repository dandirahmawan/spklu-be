package com.bppt.spklu.controller;

import com.bppt.spklu.entity.TranTracking;
import com.bppt.spklu.model.ResponseRest;
import com.bppt.spklu.service.TranTrackingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.function.Supplier;

public abstract class CommonController {

    protected abstract boolean isSecure();

    @Autowired
    private TranTrackingService tranTrackingService;

    protected <T> ResponseRest<T> res(HttpServletRequest req, HttpServletResponse res, Object body, Supplier<T> func) {
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

        if (isSecure()) {

        } else {

        }

        ResponseRest<T> r = new ResponseRest<T>("success", true, func.get());
        return r;
    }

}
