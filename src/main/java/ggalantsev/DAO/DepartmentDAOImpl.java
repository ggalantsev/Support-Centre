package ggalantsev.DAO;

import ggalantsev.Entity.Department;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository("MySQLDepartments")
public class DepartmentDAOImpl implements DepartmentDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void add(Department department) {
        entityManager.merge(
                new Department(
                        department.getName(),
                        department.getDescription(),
                        department.getSlug()));
    }

    @Override
    public Department getByID(int id) {
        return entityManager.find(Department.class, id);
    }

    @Override
    public Department getBySlug(String slug) {
        Department department = null;
        try {
            department = entityManager.createQuery(
                    "select d from Department d where d.slug = :slug",
                    Department.class).setParameter("slug", slug)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
        return department;
    }

    @Override
    public void update(Department department) {
        if(department.getId()!=0)
            entityManager.merge(department);
    }

    @Override
    public void delete(int id) {
        entityManager.remove(
                entityManager.getReference(
                        Department.class, id));
    }

    @Override
    public List<Department> getAll() {
        return entityManager.createQuery(
                "select d from Department d " +
                   "order by d.name", Department.class)
                .getResultList();
    }

}
