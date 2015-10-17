package com.danilov.versionmonitor.model;

/**
 * Created by Semyon on 17.10.2015.
 */
public class LoginResponse {

    private String key;

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
