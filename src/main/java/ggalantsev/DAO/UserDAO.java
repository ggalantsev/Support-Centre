package ggalantsev.DAO;

import ggalantsev.Entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserDAO extends CrudRepository<User, Long> {

    User findByUsername(String name);

    List<User> findAll();

    User save(User user);

    User getById(int id);

//    @Query("select u.username from User u where id=:id")
//    String getUsernameById(@Param("id") int id);

    @Query("select count(u)>0 from User u where u.username like :username")
    boolean existsByUsername(@Param("username") String username);

    void delete(User user);
}
