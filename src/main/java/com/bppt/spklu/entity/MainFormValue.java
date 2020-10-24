package com.bppt.spklu.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "main_form_value")
public class MainFormValue extends BaseEntity {

    private String name;
    private String value;
    private String description;

}
