package com.bppt.spklu.controller;

import com.bppt.spklu.entity.UserType;
import com.bppt.spklu.repo.UserTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user_type")
public class UserTypeController {
    @Autowired
    UserTypeRepo utr;

    @GetMapping
    public List<UserType> getAll(){
        return utr.findAll();
    }
}
