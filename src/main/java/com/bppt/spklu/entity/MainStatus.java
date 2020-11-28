package com.bppt.spklu.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "main_status")
@NoArgsConstructor
public class MainStatus extends BaseEntity {

    private String name;
    private String description;

    public MainStatus(Integer id) {
        setId(id);
    }

}
