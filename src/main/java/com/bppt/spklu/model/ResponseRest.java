package com.bppt.spklu.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseRest<T> {

    private String message;
    private Boolean success;
    private T data;
}
