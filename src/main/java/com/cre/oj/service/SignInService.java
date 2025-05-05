package com.cre.oj.service;



import com.cre.oj.model.entity.SignInRecord;

import java.util.List;


public interface SignInService {
    boolean signIn(Long userId);

    List<SignInRecord> getSignedDates(Long userId);
}
