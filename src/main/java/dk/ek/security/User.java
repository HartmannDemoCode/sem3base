package dk.ek.security;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mindrot.jbcrypt.BCrypt;

import javax.management.relation.Role;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name="users")
public class User implements ISecurityUser{
    @Id
    @Column(name = "username", nullable = false)
    private String username;
    private String password;

    public User(String username, String password){
        this.username = username;
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        this.password = hashed;
    }



    @Override
    public boolean verifyPassword(String pw) {
        return BCrypt.checkpw(pw, password);
    }

    @Override
    public void addRole(Role role) {

    }

    @Override
    public void removeRole(String role) {

    }
}
