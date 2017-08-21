package ggalantsev.DAO;

import ggalantsev.Entity.Department;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
@Qualifier(value = "MySQLDepartments")
//@Qualifier(value = "FakeDep")
public interface DepartmentDAO {

    void add(Department department);
    Department getByID(int id);
    Department getBySlug(String slug);
    void update(Department department);
    void delete(int id);
    List<Department> getAll();

}
