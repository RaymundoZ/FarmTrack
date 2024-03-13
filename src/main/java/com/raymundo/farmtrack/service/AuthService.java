package com.raymundo.farmtrack.service;

import com.raymundo.farmtrack.dto.AuthDto;
import com.raymundo.farmtrack.dto.UserInfoDto;

public interface AuthService {

    UserInfoDto authenticate(AuthDto authDto);

    UserInfoDto registerUser(UserInfoDto userInfoDto);

    UserInfoDto blockUser(String userEmail);

    UserInfoDto unblockUser(String userEmail);
}
