package com.raymundo.farmtrack.service;

import com.raymundo.farmtrack.dto.AuthDto;
import com.raymundo.farmtrack.dto.UserInfoDto;

public interface AuthService {

    UserInfoDto authenticate(AuthDto authDto);

    UserInfoDto register(UserInfoDto userInfoDto);
}
