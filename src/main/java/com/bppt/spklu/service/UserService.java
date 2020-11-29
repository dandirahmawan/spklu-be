package com.bppt.spklu.service;

import com.bppt.spklu.config.ResErrExc;
import com.bppt.spklu.entity.MainStatus;
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

    public ResponseLogin login(String username, String password) throws ResErrExc {
        MainUser mainUser = mainUserRepo.findFirstByUsername(username);
        String encPass = CryptoService.encPass(password);

        if (mainUser != null && mainUser.getPassword().equals(encPass)) {
            if (mainUser.getStatus() != null && mainUser.getStatus().getId() == 1)
                return genResponseLogin(mainUser, encPass);
            throw new ResErrExc("user not active");
        }
        return null;
    }

    public ResponseLogin reg(MainUser mainUser) throws ResErrExc {
        isValidData(mainUser);

        MainUser userDb = mainUserRepo.findFirstByUsername(mainUser.getUsername());

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
            s.setStatus(new MainStatus(1));

            s = mainUserRepo.saveAndFlush(s);
            return genResponseLogin(s, encPass);
        }
    }

    public ResponseLogin update(MainUser mainUser) throws ResErrExc {
        isValidData(mainUser);

        MainUser userDb = mainUserRepo.findFirstByUsername(mainUser.getUsername());

        if (userDb == null) {
            throw new ResErrExc("data not available");
        } else {
            userDb.setBod(mainUser.getBod());
            userDb.setEmail(mainUser.getEmail());

            String encPass = CryptoService.encPass(mainUser.getPassword());
            userDb.setPassword(encPass);
            userDb.setUsername(mainUser.getUsername());

            userDb.setUpdateDate(new Date());

            userDb = mainUserRepo.saveAndFlush(userDb);
            return genResponseLogin(userDb, encPass);
        }
    }

    public String disableUser(String username, boolean active) throws ResErrExc {
        MainUser userDb = mainUserRepo.findFirstByUsername(username);
        if (userDb != null) {
            if (active) userDb.setStatus(new MainStatus(1));
            else userDb.setStatus(new MainStatus(0));
            mainUserRepo.saveAndFlush(userDb);
            return "user " + username + " is " + (active ? "" : "not ") + "active";
        } else {
            throw new ResErrExc("user not found");
        }
    }

    private boolean isValidData(MainUser mainUser) throws ResErrExc {
        if (mainUser.getUsername() == null || mainUser.getPassword() == null
                || mainUser.getUsername().isEmpty() || mainUser.getPassword().isEmpty())
            throw new ResErrExc("complete the data");
        if (mainUser.getPassword().length() < 4) throw new ResErrExc("password min 4 character");
        return true;
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

    public String getUsernameByToken(String token) {
        try {
            return CryptoService.getUserPass(token)[0];
        } catch (Exception e) {
            return null;
        }
    }

}
