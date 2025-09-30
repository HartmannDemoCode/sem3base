package dk.ek.security;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Set;

@Entity
@Getter
@Setter
@Table(name="users")
public class User {
    @Id
    @Column(name = "username", nullable = false)
    private String username;
    private String password;
    @ManyToMany
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<Role> roles;

    public User() {
    }
    public User(String username, String password) {
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt(12));
        this.username = username;
        this.password = hashed;
    }

    public boolean checkPassword(String candidate){
        if (BCrypt.checkpw(candidate, password))
            return true;
        else
            return false;
    }

    public void addRole(Role role){
        this.roles.add(role);
        role.getUsers().add(this);
    }

    public static void main(String[] args) {
        User user = new User("user1", "pass123");
        System.out.println(user.username+": "+user.password);
    }
}
