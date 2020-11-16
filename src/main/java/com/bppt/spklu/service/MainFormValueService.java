package com.bppt.spklu.service;

import com.bppt.spklu.entity.MainFormValue;
import com.bppt.spklu.repo.MainFormValueRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MainFormValueService {

    @Autowired
    private MainFormValueRepo mainFormValueRepo;

    public List<MainFormValue> getAll() {
        return mainFormValueRepo.findAll();
    }

    public MainFormValue getOneByName(String name) {
        return mainFormValueRepo.findFirstByName(name);
    }

    public MainFormValue save(MainFormValue mainFormValue) {
        MainFormValue mfv = getOneByName(mainFormValue.getName());
        if (mfv == null) mfv = new MainFormValue();
        mfv.setName(mainFormValue.getName());
        mfv.setValue(mainFormValue.getValue());
        mfv.setDescription(mainFormValue.getDescription());
        mfv.setCreateDate(mfv.getId() == null ? new Date() : mfv.getCreateDate());
        mfv.setUpdateDate(new Date());
        return mainFormValueRepo.saveAndFlush(mfv);
    }

    public List<MainFormValue> saves(List<MainFormValue> mainFormValue) {
        List<MainFormValue> data = new ArrayList<>();
        for(int i = 0;i<mainFormValue.size();i++){
            MainFormValue mfv = getOneByName(mainFormValue.get(i).getName());
            if (mfv == null) mfv = new MainFormValue();
            mfv.setName(mainFormValue.get(i).getName());
            mfv.setValue(mainFormValue.get(i).getValue());
            mfv.setDescription(mainFormValue.get(i).getDescription());
            mfv.setCreateDate(mfv.getId() == null ? new Date() : mfv.getCreateDate());
            mfv.setUpdateDate(new Date());
            data.add(mainFormValueRepo.saveAndFlush(mfv));
        }
        return data;
    }

}
