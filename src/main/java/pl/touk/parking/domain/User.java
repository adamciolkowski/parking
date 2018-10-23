package pl.touk.parking.domain;

import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.Entity;

import static java.util.Collections.singleton;

@Entity
public class User extends AbstractPersistable<Integer> {

    private String username;
    private String passwordHash;
    private String role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Authentication getAuthentication() {
        return new UsernamePasswordAuthenticationToken(
                username,
                passwordHash,
                singleton(new SimpleGrantedAuthority("ROLE_" + role))
        );
    }
}
