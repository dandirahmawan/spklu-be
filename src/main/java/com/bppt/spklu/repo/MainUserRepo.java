package com.bppt.spklu.repo;

import com.bppt.spklu.model.MainUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MainUserRepo extends JpaRepository<MainUser, Integer> {

    MainUser findFirstByUsername(String username);

}
