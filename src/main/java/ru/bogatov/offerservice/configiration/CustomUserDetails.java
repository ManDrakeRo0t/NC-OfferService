package ru.bogatov.offerservice.configiration;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.bogatov.offerservice.entity.Role;


import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class CustomUserDetails implements UserDetails{

    private String login;
    private Set<Role> grantedAuthorities = new HashSet<>();

    public static CustomUserDetails fromM2tToken(){
        CustomUserDetails customUserDetails = new CustomUserDetails();
        customUserDetails.login = "M2M";
        customUserDetails.grantedAuthorities.add(Role.USER);
        customUserDetails.grantedAuthorities.add(Role.ADMIN);
        return customUserDetails;
    }

    public static CustomUserDetails fromGateWayToken(String login,Set<Role> roleSet){
        CustomUserDetails customUserDetails = new CustomUserDetails();
        customUserDetails.login = login;
        customUserDetails.grantedAuthorities = roleSet;
        return customUserDetails;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return "null";
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
