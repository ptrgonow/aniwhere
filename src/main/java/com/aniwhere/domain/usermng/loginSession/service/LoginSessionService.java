package com.aniwhere.domain.usermng.loginSession.service;

import com.aniwhere.domain.usermng.loginSession.domain.LoginSession;
import com.aniwhere.domain.usermng.mapper.UserMngMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginSessionService {

    private UserMngMapper userMngMapper;

}
