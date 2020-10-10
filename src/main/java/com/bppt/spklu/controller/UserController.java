package com.bppt.spklu.controller;

import com.bppt.spklu.model.MainUser;
import com.bppt.spklu.model.ResponseRest;
import com.bppt.spklu.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController<dd> {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseRest<MainUser> login(@RequestBody Map<String, String> body) {
        ResponseRest<MainUser> res = new ResponseRest<>("username or password not found", false, null);
        String username = "";
        String password = "";

        if (body.get("username") != null || body.get("password") != null) {
            username = body.get("username");
            password = body.get("password");
        } else {
            return res;
        }

        ResponseRest<MainUser> user = userService.login(username, password);
        if (user.getSuccess())
            return new ResponseRest<>("success", true, user.getData());
        return res;
    }

}
