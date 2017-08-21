package ggalantsev.DAO;

import ggalantsev.Entity.Department;
import ggalantsev.Entity.Issue;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository("FakeDep")
public class DepartmentDAOImplFake implements DepartmentDAO {

    @PersistenceContext
    private EntityManager entityManager;

    private static Map<Integer,Department> departments = new HashMap<Integer, Department>() {
        {
            put(1, new Department("s1", "s1 text", "s1"));
            put(2, new Department("s2", "s2 text", "s2"));
            put(3, new Department("s3", "s3 text", "s3"));
        }
    };


    @Override
    public void add(Department department) {
    }

    @Override
    public Department getByID(int id) {
        System.out.println("Fake DAO getByID");
        return departments.get(id);
    }

    @Override
    public Department getBySlug(String slug) {
        return null;
    }

    @Override
    public void update(Department department) {
    }

    @Override
    public void delete(int id) {
    }

    @Override
    public List<Department> getAll() {
        System.out.println("Fake DAO getall");
        return departments.values().stream().collect(Collectors.toList());
    }

}
