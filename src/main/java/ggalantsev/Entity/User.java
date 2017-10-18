package ggalantsev.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

// Lombok make code shorter =)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "user_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private List<UserRole> authorities;

    @NotNull
    @Column(name = "password")
    private String password;

    @NotNull
    @Column(name = "username", unique = true)
    private String username;

    @Column
    private String name;

    @Column
    private String surname;

    @Column(name = "accountNonExpired", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean accountNonExpired;

    @Column(name = "accountNonLocked", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean accountNonLocked;

    @Column(name = "credentialsNonExpired", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean credentialsNonExpired;

    @Column(name = "enabled", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean enabled;

}
