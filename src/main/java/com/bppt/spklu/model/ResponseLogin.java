package com.bppt.spklu.model;

import com.bppt.spklu.entity.MainStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseLogin {

    private String username;
    private MainStatus status;
    private String email;
    private Date bod;
    private String token;

}
