package com.bppt.spklu.controller;

import com.bppt.spklu.config.ResErrExc;
import com.bppt.spklu.entity.MainUser;
import com.bppt.spklu.model.ReqLogin;
import com.bppt.spklu.model.ResponseLogin;
import com.bppt.spklu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/user")
public class UserController extends CommonController {

    @Override
    protected boolean isSecure() {
        return true;
    }

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity login(HttpServletRequest req, HttpServletResponse res, @RequestBody ReqLogin body) {
        req.setAttribute("isSecure", false);
        req.setAttribute("isWithoutUserType", false);
        return res(req, res, body, () -> {
            if (body.getUsername() == null || body.getPassword() == null
                || body.getUsername().isEmpty() || body.getPassword().isEmpty())
                throw new ResErrExc("username & password not empty");

            ResponseLogin s = userService.login(body.getUsername(), body.getPassword());
            if (s != null) return s;
            else throw new ResErrExc("user not found");
        });
    }

    @PostMapping("/reg")
    public ResponseEntity reg(HttpServletRequest req, HttpServletResponse res, @RequestBody MainUser body) {
        req.setAttribute("isSecure", false);
        req.setAttribute("isWithoutUserType", false);
        return res(req, res, body, () -> userService.reg(body));
    }

    @PutMapping("/update")
    public ResponseEntity update(HttpServletRequest req, HttpServletResponse res, @RequestBody MainUser body) {
        return res(req, res, body, () -> userService.update(body));
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity switchStatus(HttpServletRequest req, HttpServletResponse res,
                                       @PathVariable String username, @Param("active") Boolean active) {
        return res(req, res, null, () -> {
            if (active != null && active) return userService.disableUser(username, true);
            return userService.disableUser(username, false);
        });
    }

}
