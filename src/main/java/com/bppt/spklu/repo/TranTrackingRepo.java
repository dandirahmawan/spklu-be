package com.bppt.spklu.repo;

import com.bppt.spklu.entity.TranTracking;
import com.bppt.spklu.entity.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface TranTrackingRepo extends JpaRepository<TranTracking, Integer> {

    @Query("select t from TranTracking t where t.createDate > ?1 and t.createDate < ?2 and t.userType = ?3 " +
            "and t.requestUri like '/api/admin/form-value' and t.user is null and t.userType is not null")
    List<TranTracking> findBySpecific(Date startDate, Date endDate, UserType userType);

    @Query("select t from TranTracking t where t.createDate > ?1 and t.createDate < ?2 and t.requestUri like " +
            "'/api/admin/form-value' and t.user is null and t.userType is not null")
    List<TranTracking> findBySpecificWoUserType(Date startDate, Date endDate);

}
