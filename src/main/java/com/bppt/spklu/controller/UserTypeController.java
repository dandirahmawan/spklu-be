package com.bppt.spklu.controller;

import com.bppt.spklu.entity.UserType;
import com.bppt.spklu.service.UserTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping({"/api/user-type", "/api/user_type"})
public class UserTypeController extends CommonController {


    @Override
    protected boolean isSecure() {
        return true;
    }

    @Autowired
    private UserTypeService userTypeService;

    @GetMapping
    public ResponseEntity getAllUserType(HttpServletRequest req, HttpServletResponse res) {
        req.setAttribute("isSecure", false);
        req.setAttribute("isWithoutUserType", false);
        return res(req, res, null, () -> userTypeService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity getUserTypeById(HttpServletRequest req, HttpServletResponse res, @PathVariable Integer id) {
        req.setAttribute("isSecure", false);
        req.setAttribute("isWithoutUserType", false);
        return res(req, res, null, () -> userTypeService.getById(id));
    }

    @PostMapping
    public ResponseEntity saveUserType(HttpServletRequest req, HttpServletResponse res, @RequestBody UserType userType) {
        return res(req, res, null, () -> userTypeService.save(userType));
    }

}
