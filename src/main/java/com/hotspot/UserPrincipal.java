package com.hotspot;

import lombok.Data;

import java.security.Principal;

@Data
public class UserPrincipal implements Principal {
    private String id;
    private String userName;

    @Override
    public String getName() {
        return this.userName;
    }
}