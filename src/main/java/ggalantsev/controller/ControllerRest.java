package ggalantsev.controller;

import ggalantsev.Entity.*;
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

    //API test
    @RequestMapping(value = "/api", method = RequestMethod.GET)
    public String index(@RequestParam(value = "name",defaultValue = "none") String name) {
        return "hello: "+name;
    }

    @RequestMapping(value = "/api/issues")
    public List<IssueJSON> getIssuesJSON() {
        List<IssueJSON> issues = new ArrayList<>();
        issueService.getAllIssues().stream()
                .forEach(issue -> issues.add(new IssueJSON(issue)));
        issues.stream().forEach(issueJSON -> System.out.println(issueJSON.toString()));
        return issues;
    }

    @RequestMapping(value = "/api/departments")
    public List<DepartmentJSON> getDepartmentsJSON() {
        List<DepartmentJSON> departmentJSON = new ArrayList<>();
        departmentService.getAll().stream()
                .forEach(department -> departmentJSON.add(new DepartmentJSON(department)));
        return departmentJSON;
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

    // https://www.youtube.com/watch?v=oTE6ylQgngM&t=821s
}
