package com.contacts.common.message;

public enum MessageSrcCode {
    CONTACT_SAVE_FAIL("contact_save_fail"),
    CONTACT_SAVE_SUCCESS("contact_save_success"),
    CONTACT_DELETE_SUCCESS("contact_delete_success"),
    MESSAGE_LOGIN_FAIL("message_login_fail");

    private final String code;

    MessageSrcCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return this.code;
    }
}
