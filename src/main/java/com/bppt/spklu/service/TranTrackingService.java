package com.bppt.spklu.service;

import com.bppt.spklu.entity.TranTracking;
import com.bppt.spklu.repo.TranTrackingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TranTrackingService {

    @Autowired
    private TranTrackingRepo tranTrackingRepo;

    public TranTracking save(TranTracking tranTracking) {
        return tranTrackingRepo.saveAndFlush(tranTracking);
    }

}
