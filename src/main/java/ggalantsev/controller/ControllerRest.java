package ggalantsev.controller;

import ggalantsev.Entity.Department;
import ggalantsev.Entity.Issue;
import ggalantsev.Entity.IssueAutocompleteResponse;
import ggalantsev.Service.DepartmentService;
import ggalantsev.Service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
public class ControllerRest {

    @Autowired
    private IssueService issueService;

    @Autowired
    private DepartmentService departmentService;

    @RequestMapping(value = "/api", method = RequestMethod.GET)
    public String index(@RequestParam(value = "name",defaultValue = "NAME") String name) {
        return "Name is: "+name;
    }
//AJAX autocomplete controller
    @RequestMapping(value = "/search.api", method = RequestMethod.GET)
    public List<IssueAutocompleteResponse> searchApi(@RequestParam(value = "term",defaultValue = "NAME") String term) {
        List<Issue> list = issueService.searchIssues(term);
        List<IssueAutocompleteResponse> responseList = new ArrayList<>();
        for (Issue issue : list) {
            IssueAutocompleteResponse response = new IssueAutocompleteResponse();
                response.setLabel(issue.getName());
                response.setValue(issue.getName() + "");
                response.setUrl("/issue/" + issue.getId());
            responseList.add(response);
        }
        return responseList;
    }

    @RequestMapping(value = "/api2")
    public Department index2() {
        return departmentService.get(1);
    }

    @RequestMapping(value = "/api3")
    public List<Department> index3() {
        return departmentService.getAll();
    }

    @RequestMapping(value = "/issue/api/{id}")
    public Issue getIssue(@PathVariable(value = "id") int id){
        System.err.println("--- >>> GET REST");
        System.err.println(id);
        return issueService.getIssue(id);
    }

    @RequestMapping(value = "/issue/api/childs", method = RequestMethod.GET)
    public List<Issue> getAllIssues(@RequestParam(name = "pid",defaultValue = "-1") int pid){
        if (pid==-1)
            return issueService.getAllIssues();
        return issueService.getIssuesChilds(pid);
    }

    @RequestMapping(value = "/issue/api", method = RequestMethod.POST)
    public void addIssue(@RequestBody Issue issue){
        issueService.addIssue(issue);
    }

    @RequestMapping(value = "/issue/api", method = RequestMethod.PUT)
    public void updateIssue(@RequestBody Issue issue){
        issueService.updateIssue(issue);
    }

    @RequestMapping(value = "/issue/api/{id}", method = RequestMethod.DELETE)
    public void deleteIssue(@PathVariable(value = "id") int id){
        issueService.deleteIssue(id);
    }


//
//    ----- >>>>> https://www.youtube.com/watch?v=oTE6ylQgngM&t=821s
//

}
