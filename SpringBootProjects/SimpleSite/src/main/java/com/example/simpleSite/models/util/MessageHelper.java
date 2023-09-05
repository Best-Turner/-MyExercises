package com.example.simpleSite.models.util;

import com.example.simpleSite.models.User;

public abstract class MessageHelper {
    public static String getAuthorName(User user) {
        return user != null ? user.getUsername() : "<none>";
    }
}
