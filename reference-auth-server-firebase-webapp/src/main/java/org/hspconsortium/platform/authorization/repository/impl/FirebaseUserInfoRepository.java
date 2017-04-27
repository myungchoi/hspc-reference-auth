package org.hspconsortium.platform.authorization.repository.impl;

import org.hspconsortium.platform.authentication.persona.PersonaUserInfoRepository;
import org.hspconsortium.platform.service.FirebaseTokenService;
import org.mitre.openid.connect.model.DefaultUserInfo;
import org.mitre.openid.connect.model.UserInfo;

import javax.inject.Inject;

public class FirebaseUserInfoRepository extends PersonaUserInfoRepository {
    @Inject
    private FirebaseTokenService firebaseTokenService;

    @Override
    public UserInfo getRealUserByUsername(String username) {
        // validate username against Firebase

        UserInfo userInfo = new DefaultUserInfo();

        userInfo.setSub(username);
        userInfo.setPreferredUsername(username);
        userInfo.setEmail(username);
        userInfo.setName(username);

        return userInfo;
    }
}
