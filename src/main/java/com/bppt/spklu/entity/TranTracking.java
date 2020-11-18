package com.bppt.spklu.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "tran_tracking")
public class TranTracking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "create_date")
    private Date createDate;
    @Column(name = "ip_address")
    private String ipAddress;
    @Column(name = "request_uri")
    private String requestUri;
    @Column(name = "request_body")
    private String requestBody;
    private String user;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_type_id")
    private UserType userType;

}
