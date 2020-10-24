package com.bppt.spklu.service;

import com.bppt.spklu.entity.MainUser;
import com.bppt.spklu.model.ResponseRest;
import com.bppt.spklu.repo.MainUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class UserService {

    @Autowired
    private MainUserRepo mainUserRepo;

    public ResponseRest<MainUser> login(String username, String password) {
        MainUser mainUser = mainUserRepo.findFirstByUsername(username);
        if (mainUser != null
                && mainUser.getPassword().equals(Base64.getEncoder().encodeToString(password.getBytes()))) {
            return new ResponseRest<>("", true, mainUser);
        }
        return new ResponseRest<>("", false, null);
    }

}
