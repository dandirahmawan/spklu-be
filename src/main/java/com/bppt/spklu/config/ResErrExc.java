package com.bppt.spklu.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResErrExc extends Exception {

    private String msg;

}
