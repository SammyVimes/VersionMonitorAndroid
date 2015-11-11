package com.danilov.versionmonitor.model;

/**
 * Created by Semyon on 17.10.2015.
 */
public class LoginResponse {

    private String key;

    private String pushId;

    private String pushToken;

    private long pushTokenTimestamp;

    public String getPushId() {
        return pushId;
    }

    public void setPushId(final String pushId) {
        this.pushId = pushId;
    }

    public String getPushToken() {
        return pushToken;
    }

    public void setPushToken(final String pushToken) {
        this.pushToken = pushToken;
    }

    public long getPushTokenTimestamp() {
        return pushTokenTimestamp;
    }

    public void setPushTokenTimestamp(final long pushTokenTimestamp) {
        this.pushTokenTimestamp = pushTokenTimestamp;
    }

    private Boolean success;

    private LoginResult reason;

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(final Boolean success) {
        this.success = success;
    }

    public LoginResult getReason() {
        return reason;
    }

    public void setReason(final LoginResult reason) {
        this.reason = reason;
    }

    public enum LoginResult {

        WRONG_PASSWORD("неверный пароль"),
        USER_NOT_FOUND("пользователь не найден");

        private String def;

        LoginResult(final String def) {
            this.def = def;
        }

        public String getDef() {
            return def;
        }

    }

}
