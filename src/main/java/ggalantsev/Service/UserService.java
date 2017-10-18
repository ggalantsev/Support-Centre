package ggalantsev.Service;

import ggalantsev.DAO.UserDAO;
import ggalantsev.Entity.User;
import ggalantsev.Entity.UserRole;
import lombok.AllArgsConstructor;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service("userService")
@Transactional
public class UserService implements UserDetailsService {


    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserDAO userDAO;

    @PostConstruct
    @Transactional
    public void init(){
        if(userDAO.findByUsername("admin")==null) {
            List<UserRole> roles = new ArrayList<>();
            roles.add(UserRole.ROLE_USER);
            roles.add(UserRole.ROLE_ADMIN);
            userDAO.save(User.builder()
                    .username("admin")
                    .password(new BCryptPasswordEncoder().encode("pass"))
                    .authorities(roles)
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .enabled(true)
                    .build());
        }
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userDAO.findByUsername(userName);
        if (user == null) {
            log.warn("User \"" + userName + "\" not found!");
            throw new UsernameNotFoundException("User \"" + userName + "\" not found!");
        }
        return user;
    }

    @Transactional
    public List<User> getAll(){
        return userDAO.findAll();
    }

    @Transactional
    public User getById(int id){
        return userDAO.getById(id);
    }

    @Transactional
    public User save(User user){
        return userDAO.save(user);
    }

    public boolean existsByUsername(String username){
        return userDAO.existsByUsername(username);
    }

    @Transactional
    public void delete(User user){
        userDAO.delete(user);
    }

}
