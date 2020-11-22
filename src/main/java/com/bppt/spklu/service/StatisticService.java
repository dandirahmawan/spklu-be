package com.bppt.spklu.service;

import com.bppt.spklu.config.ResErrExc;
import com.bppt.spklu.entity.TranTracking;
import com.bppt.spklu.entity.UserType;
import com.bppt.spklu.model.ReqStatistic;
import com.bppt.spklu.model.ResGroupUserType;
import com.bppt.spklu.repo.TranTrackingRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class StatisticService {

    @Autowired
    private TranTrackingRepo tranTrackingRepo;

    public ReqStatistic getBySpecific(ReqStatistic reqStatistic, Integer userTypeId) throws ResErrExc {
        if (reqStatistic.getStartDate() == null
                || reqStatistic.getEndDate() == null
                || reqStatistic.getStartDate().getTime() > reqStatistic.getEndDate().getTime()) {
            throw new ResErrExc("invalid range date");
        }

        List<TranTracking> s;

        ReqStatistic r = new ReqStatistic();
        r.setStartDate(reqStatistic.getStartDate());
        r.setEndDate(reqStatistic.getEndDate());

        if (userTypeId != null) {
            UserType ut = new UserType(userTypeId);
            s = tranTrackingRepo.findBySpecific(reqStatistic.getStartDate(),
                    reqStatistic.getEndDate(), ut);
            if (s.size() > 0) {
                ResGroupUserType gr = new ResGroupUserType(s.size(), s.get(0).getUserType());
                r.setGroupUserType(Arrays.asList(gr));
            }
        } else {
            s = tranTrackingRepo.findBySpecificWoUserType(reqStatistic.getStartDate(),
                    reqStatistic.getEndDate());
            List<TranTracking> group = s.stream()
                    .filter(distinctByKey(p -> p.getUserType()))
                    .collect(Collectors.toList());
            group.forEach(e -> {
                List<TranTracking> grFill = s.stream()
                        .filter(e1 -> e1.getUserType().getId().equals(e.getUserType().getId()))
                        .collect(Collectors.toList());
                r.getGroupUserType().add(new ResGroupUserType(grFill.size(), e.getUserType()));
            });
//            s.forEach((e) -> {
//                Map<UserType, List<TranTracking>> group = s.stream().collect(Collectors.groupingBy(e1 -> e1.getUserType()));
//                group.forEach((k, v) -> r.getGroupUserType().add(new ResGroupUserType(v.size(), k)));
//            });
        }

        r.setTotalVisit(s.size());

        return r;
    }

    public ReqStatistic getToday() throws ResErrExc {
        String dayString = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Date day = null;
        try {
            day = new SimpleDateFormat("yyyy-MM-dd").parse(dayString);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new ResErrExc("error parse");
        }
        Date now = new Date();

        ReqStatistic r = new ReqStatistic();
        r.setStartDate(day);
        r.setEndDate(now);

        return getBySpecific(r, null);
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

}
