package com.delon.decoderauthentication.services.impl;

import com.delon.decoderauthentication.repositories.UserCourseRepository;
import com.delon.decoderauthentication.services.iface.IUserCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCourseService implements IUserCourseService {

    private final UserCourseRepository userCourseRepository;
}
