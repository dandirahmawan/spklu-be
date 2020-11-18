package com.bppt.spklu.service;

import com.bppt.spklu.config.ResErrExc;
import com.bppt.spklu.entity.UserType;
import com.bppt.spklu.repo.UserTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserTypeService {

    @Autowired
    private UserTypeRepo userTypeRepo;

    public List<UserType> getAll() {
        return userTypeRepo.findAll();
    }

    public UserType getById(Integer id) {
        return userTypeRepo.findById(id).orElse(null);
    }

    public UserType save(UserType userType) throws ResErrExc {
        if (userType == null || userType.getName() == null || userType.getName().isEmpty()) {
            throw new ResErrExc("invalid name");
        }
        return userTypeRepo.save(userType);
    }

}
