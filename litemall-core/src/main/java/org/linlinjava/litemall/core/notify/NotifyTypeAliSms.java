package org.linlinjava.litemall.core.notify;

public enum NotifyTypeAliSms {
    BEGIN_SEND("begin_send"),
    BUY_SUCCESS("buy_success"),
    VERIFY_CODE("verify_code");

    private String type;

    NotifyTypeAliSms(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
