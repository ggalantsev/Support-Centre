package ggalantsev.DAO;

import ggalantsev.Entity.Issue;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Repository("MySQLIssue")
public class IssueDAOImpl implements IssueDAO{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Issue add(Issue issue) {
        return entityManager.merge(new Issue(issue.getPid(), issue.getName(), issue.getContent(), issue.getDepartment()));
    }

    @Override
    public Issue getByID(int id) {
        return entityManager.find(Issue.class, id);
    }

    @Override
    public void update(Issue issue) {
        if(issue.getId()!=0)
            entityManager.merge(issue);
    }

    @Override
    public void delete(int id) {
        entityManager.remove(
                entityManager.getReference(
                        Issue.class, id
                ));
    }

    @Override
    public List<Issue> getAll() {
        return entityManager.createQuery(
                "select s from Issue s " +
                   "order by s.id",Issue.class)
                .getResultList();

    }

    @Override
    public List<Issue> getChilds(int pid) {
        return entityManager.createQuery(
                "select s from Issue s where s.pid like :pid order by s.name",
                Issue.class).setParameter("pid",pid).getResultList();
    }

    @Override
    public List<Issue> getParrentList(int pid) {
        List<Issue> sList = new ArrayList<>();
        while (pid!=0) {
            Issue s = entityManager.createQuery(
                    "select s from Issue s where s.id=:pid",
                    Issue.class).setParameter("pid", pid)
                    .getSingleResult();
            pid = s.getPid();
            sList.add(s);
        }
        Collections.reverse(sList);
        return sList;
    }

    @Override
    public List<Issue> searchIssues(String pattern) {
        return entityManager.createQuery(
                "select s from Issue s where s.content like :pattern or s.name like :pattern order by s.id",
                Issue.class).setParameter("pattern","%"+pattern+"%").getResultList();
    }
}
