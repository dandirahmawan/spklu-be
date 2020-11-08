package com.bppt.spklu.model;

import lombok.Data;

import java.util.List;

@Data
public class ReqKwhKonektor {

    private Integer evse;
    private List<ReqKonektor> konektor;

}
