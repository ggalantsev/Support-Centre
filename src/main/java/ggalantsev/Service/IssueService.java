package ggalantsev.Service;

import ggalantsev.DAO.IssueDAO;
import ggalantsev.Entity.Issue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("issueService")
@Transactional
public class IssueService {

    @Autowired
    @Qualifier("MySQLIssue")
    private IssueDAO issueDAO;

    @Transactional
    public void addIssue(Issue issue) {
        issueDAO.add(issue);
    }

    @Transactional
    public Issue getIssue(int id) {
        return issueDAO.getByID(id);
    }

    @Transactional
    public void updateIssue(Issue issue){
        issueDAO.update(issue);
    }

    @Transactional
    public void deleteIssue(int id){
        issueDAO.delete(id);
    }

    @Transactional
    public List<Issue> getAllIssues(){
        return this.issueDAO.getAll();
    }

    @Transactional
    public List<Issue> getIssuesChilds(int pid){
        return this.issueDAO.getChilds(pid);
    }

    @Transactional
    public List<Issue> getParrentList(int pid){
        return issueDAO.getParrentList(pid);
    }
    @Transactional
    public List<Issue> searchIssues(String pattern){
        return this.issueDAO.searchIssues(pattern);
    }



}
