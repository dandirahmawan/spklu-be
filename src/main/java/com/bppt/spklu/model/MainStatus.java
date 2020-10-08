package com.bppt.spklu.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "main_status")
public class MainStatus extends BaseModel {

    private String name;
    private String description;

}
