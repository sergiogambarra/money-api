package com.gambarra.money.api.security;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;


import java.util.Collection;

public class SystemUser extends User {

    private static final long serialVersionUID = 1L;

    private com.gambarra.money.api.model.User user;

    public SystemUser(com.gambarra.money.api.model.User user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getEmail(), user.getPassword(), authorities);
        this.user = user;
    }

    public com.gambarra.money.api.model.User getUser() {
        return user;
    }
}
