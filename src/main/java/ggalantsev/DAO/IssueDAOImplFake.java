package ggalantsev.DAO;

import ggalantsev.Entity.Department;
import ggalantsev.Entity.Issue;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository("Fake")
public class IssueDAOImplFake implements IssueDAO{

    private static Map<Integer,Issue> issues = new HashMap<Integer, Issue>() {
            {
                put(1, new Issue(1, "s1", "s1 text", new Department("No","Department","here")));
                put(2, new Issue(2, "s2", "s2 text", new Department("No","Department","here")));
                put(3, new Issue(3, "s3", "s3 text", new Department("No","Department","here")));
            }
        };


    @Override
    public void add(Issue issue) {
        for (int i = issues.size(); i < 200000; i++) {
            if (this.issues.containsKey(i)){
                continue;
            } this.issues.put(i,issue);
        }

    }

    @Override
    public Issue getByID(int id) {
        System.err.println("--- >>> GET DAO Fake");
        return this.issues.get(id);
    }

    @Override
    public void update(Issue issue) {
        this.issues.put(issue.getId(),issue);
    }

    @Override
    public void delete(int id) {
        this.issues.remove(id);
    }

    @Override
    public List<Issue> getAll() {
        return issues.values().stream().collect(Collectors.toList());
    }

    @Override
    public List<Issue> getChilds(int pid) {
        return null;
    }

    @Override
    public List<Issue> getParrentList(int id) {
        return null;
    }

    @Override
    public List<Issue> searchIssues(String pattern) {
        return null;
    }
}
