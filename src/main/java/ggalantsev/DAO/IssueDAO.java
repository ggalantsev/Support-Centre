package ggalantsev.DAO;

import ggalantsev.Entity.Issue;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

public interface IssueDAO {

    Issue add(Issue issue);
    Issue getByID(int id);
    void update(Issue issue);
    void delete(int id);
    List<Issue> getAll();
    List<Issue> getChilds(int pid);
    List<Issue> getParrentList(int pid);
    List<Issue> searchIssues(String pattern);
}
