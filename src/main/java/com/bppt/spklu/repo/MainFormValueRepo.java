package com.bppt.spklu.repo;

import com.bppt.spklu.entity.MainFormValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MainFormValueRepo extends JpaRepository<MainFormValue, Integer> {

    MainFormValue findFirstByName(String name);

}
