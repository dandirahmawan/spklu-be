package com.bppt.spklu.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "main_status")
public class MainStatus extends BaseEntity {

    private String name;
    private String description;

}
