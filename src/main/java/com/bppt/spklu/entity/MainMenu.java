package com.bppt.spklu.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "main_menu")
public class MainMenu extends BaseEntity {

    private String menu;
    private String path;

}
