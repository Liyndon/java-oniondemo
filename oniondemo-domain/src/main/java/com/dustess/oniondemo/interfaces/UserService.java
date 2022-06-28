package com.dustess.oniondemo.interfaces;

import com.dustess.oniondemo.entity.user.UserInfo;

import java.util.List;

public interface UserService {
    UserInfo get(String id);
    List<UserInfo> getSome(List<String> ids);
}
