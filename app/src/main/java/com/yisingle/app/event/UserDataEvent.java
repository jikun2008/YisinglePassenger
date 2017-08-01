package com.yisingle.app.event;

import com.yisingle.app.data.UserData;

/**
 * Created by jikun on 17/7/31.
 */

public class UserDataEvent {

    private UserData userData;

    public UserDataEvent(UserData userData) {
        this.userData = userData;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }
}
