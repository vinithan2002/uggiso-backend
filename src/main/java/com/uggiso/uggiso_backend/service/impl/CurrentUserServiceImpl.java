package com.uggiso.uggiso_backend.service.impl;

import com.uggiso.uggiso_backend.entity.Users;
import com.uggiso.uggiso_backend.service.CurrentUserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserServiceImpl implements CurrentUserService {

    @Override
    public Users getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        return (Users) authentication.getPrincipal();
    }
}