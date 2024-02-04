package ru.itmo.wp.form;

import org.springframework.stereotype.Component;

@Component
public class UserStatus {
    private long id;

    private boolean status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
