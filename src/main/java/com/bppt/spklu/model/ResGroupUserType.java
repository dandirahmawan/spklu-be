package com.bppt.spklu.model;

import com.bppt.spklu.entity.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResGroupUserType {

    private Integer totalVisit;
    private UserType userType;

}
