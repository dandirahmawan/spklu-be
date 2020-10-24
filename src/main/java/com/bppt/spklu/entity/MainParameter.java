package com.bppt.spklu.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "main_parameter")
public class MainParameter extends BaseEntity {

    private String key;
    private String value;
    private String type;
    private String description;

}
