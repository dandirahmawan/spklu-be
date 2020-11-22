package com.bppt.spklu.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "user_type")
@JsonIgnoreProperties("hibernateLazyInitializer")
public class UserType {

    @Id
    private Integer id;
    private String name;

    public UserType() {}
    public UserType(Integer id) {
        this.id = id;
    }

}
