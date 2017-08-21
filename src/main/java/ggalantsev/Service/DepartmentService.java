package ggalantsev.Service;

import ggalantsev.DAO.DepartmentDAO;
import ggalantsev.Entity.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("departmentService")
@Transactional
public class DepartmentService {

    @Autowired
    @Qualifier("MySQLDepartments")
//    @Qualifier("FakeDep")
    private DepartmentDAO departmentDAO;

    public void add(Department department){
        departmentDAO.add(department);
    }

    public Department getByID(int id){
        return departmentDAO.getByID(id);
    }

    public Department getBySlug(String slug){
        return departmentDAO.getBySlug(slug);
    }

    public void update(Department department){
        departmentDAO.update(department);
    }

    public void delete(int id){
        departmentDAO.delete(id);
    }

    public List<Department> getAll(){
        return departmentDAO.getAll();
    }

}
