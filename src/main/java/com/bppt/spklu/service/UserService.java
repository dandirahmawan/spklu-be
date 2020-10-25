package com.bppt.spklu.service;

import com.bppt.spklu.config.ResErrExc;
import com.bppt.spklu.entity.MainUser;
import com.bppt.spklu.model.ResponseLogin;
import com.bppt.spklu.repo.MainUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {

    @Autowired
    private MainUserRepo mainUserRepo;

    public ResponseLogin login(String username, String password) {
        MainUser mainUser = mainUserRepo.findFirstByUsername(username);
        String encPass = CryptoService.encPass(password);

        if (mainUser != null && mainUser.getPassword().equals(encPass)) {
            return genResponseLogin(mainUser, encPass);
        }
        return null;
    }

    public ResponseLogin reg(MainUser mainUser) throws ResErrExc {
        MainUser userDb = mainUserRepo.findFirstByUsername(mainUser.getUsername());

        if (mainUser.getPassword().length() < 4) throw new ResErrExc("password min 4 character");

        if (userDb != null) {
            throw new ResErrExc("username not available");
        } else {
            MainUser s = new MainUser();
            s.setBod(mainUser.getBod());
            s.setEmail(mainUser.getEmail());

            String encPass = CryptoService.encPass(mainUser.getPassword());
            s.setPassword(encPass);
            s.setUsername(mainUser.getUsername());
            s.setCreateDate(new Date());

            s = mainUserRepo.saveAndFlush(s);
            return genResponseLogin(s, encPass);
        }
    }

    private ResponseLogin genResponseLogin(MainUser mainUser, String encPass) {
        ResponseLogin res = new ResponseLogin();
        res.setUsername(mainUser.getUsername());
        res.setEmail(mainUser.getEmail());
        res.setBod(mainUser.getBod());
        res.setStatus(mainUser.getStatus());

        String token = CryptoService.genToken(res.getUsername(), encPass);
        res.setToken(token);

        return res;
    }

    public boolean validToken(String token) {
        try {
            String[] u = CryptoService.getUserPass(token);
            ResponseLogin s = login(u[0], CryptoService.decPass(u[1]));
            return s != null;
        } catch (Exception e) {
            return false;
        }
    }

}
