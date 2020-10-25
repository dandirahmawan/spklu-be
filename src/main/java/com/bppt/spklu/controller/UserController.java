package com.bppt.spklu.controller;

import com.bppt.spklu.config.ResErrExc;
import com.bppt.spklu.entity.MainUser;
import com.bppt.spklu.model.ResponseLogin;
import com.bppt.spklu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController extends CommonController {

    @Override
    protected boolean isSecure() {
        return false;
    }

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity login(HttpServletRequest req, HttpServletResponse res, @RequestBody Map<String, String> body) {
        return res(req, res, body, () -> {
            String username = "";
            String password = "";

            if (body.get("username") != null || body.get("password") != null) {
                username = body.get("username");
                password = body.get("password");
            } else {
                throw new ResErrExc("username & password not empty");
            }
            ResponseLogin s = userService.login(username, password);
            if (s != null) return s;
            else throw new ResErrExc("user not found");
        });
    }

    @PostMapping("/reg")
    public ResponseEntity reg(HttpServletRequest req, HttpServletResponse res, @RequestBody MainUser body) {
        return res(req, res, body, () -> userService.reg(body));
    }

}
