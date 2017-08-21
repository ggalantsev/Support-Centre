package ggalantsev.controller;

import ggalantsev.Entity.Department;
import ggalantsev.Entity.Issue;
import ggalantsev.Service.DepartmentService;
import ggalantsev.Service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@org.springframework.stereotype.Controller
public class Controller {

    @Autowired
    private IssueService issueService;

    @Autowired
    private DepartmentService departmentService;

    @RequestMapping("/")
    public String main(Model model) {
        model.addAttribute("departments", departmentService.getAll());
        return "index";
    }

    //Admin pages controller
    @RequestMapping("/admin")
    public String adminPage(Model model) {
        model.addAttribute("departments", departmentService.getAll());
        return "admin/index" ;
    }

    @RequestMapping("/admin/departments")
    public String adminDepartmentsPage(Model model, HttpServletRequest request) {
        model.addAttribute("departments", departmentService.getAll());
        model.addAttribute("Alert", request.getParameter("Alert"));
        return "admin/departments";
    }

    @RequestMapping("/admin/department/{slug}")
    public String adminDepartmentPage(@PathVariable(value = "slug") String slug, Model model) {
        model.addAttribute("departments", departmentService.getAll());
        model.addAttribute("department", departmentService.getBySlug(slug));
        List<Issue> issues = departmentService.getBySlug(slug).getIssues()
                .stream().filter(issue -> issue.getPid() == 0)
                .collect(Collectors.toList());
        Map issuesMap = new HashMap<Issue,Integer>();
        for (Issue i : issues) {
            issuesMap.put(i,issueService.getIssuesChilds(i.getId()).size());
        }
        model.addAttribute("issues", issuesMap);
        return "admin/department";
    }

    @RequestMapping("/admin/department/add")
    public String adminDepartmentAddPage(Model model){
        model.addAttribute("departments",departmentService.getAll());
        return "admin/department-add";
    }

    @RequestMapping(value = "/admin/department/add", method = RequestMethod.POST)
    public String adminDepartmentAdd(Model model, HttpServletRequest request){
        boolean departmentExist = true;
        try{
            departmentService.getBySlug(request.getParameter("departmentSlug"));
        } catch (EmptyResultDataAccessException | NoResultException e) {
            departmentExist = false;
        }
        if (departmentExist){
            model.addAttribute("departments",departmentService.getAll());
            model.addAttribute("alert", "<b>Error.</b> Department with slug \"" +
                    request.getParameter("departmentSlug") + "\" already exist");
            model.addAttribute("departmentName", request.getParameter("departmentName"));
            model.addAttribute("departmentDescription", request.getParameter("departmentDescription"));
            model.addAttribute("departmentSlug", request.getParameter("departmentSlug"));
            return "admin/department-add";
        } else {
            Department d = new Department(
                    request.getParameter("departmentName"),
                    request.getParameter("departmentDescription"),
                    request.getParameter("departmentSlug")
            );
            departmentService.add(d);
            return "redirect:/admin/department/" + request.getParameter("departmentSlug");
        }
    }

    @RequestMapping(value = "/admin/department/edit/{id}", method = RequestMethod.POST)
    public String adminDepartmentEdit(@PathVariable(value = "id") int id, HttpServletRequest request) {
        Department d = departmentService.getByID(id);
        d.setName(request.getParameter("departmentName"));
        d.setSlug(request.getParameter("departmentSlug"));
        d.setDescription(request.getParameter("departmentDescription"));

        departmentService.update(d);

        return "redirect:/admin/department/" + request.getParameter("departmentSlug");
    }

    @RequestMapping("/admin/department/remove/{id}")
    public String adminDepartmentRemove(@PathVariable(value = "id") int id, HttpServletRequest httpServletRequest, Model model){
        // TODO: move to trash
        model.addAttribute("Alert","Department " + departmentService.getByID(id).getName() +
                " successfully removed");
        departmentService.delete(id);
        return "redirect:/admin/departments";
    }

    @RequestMapping("/admin/issues")
    public String adminIssuesPage(Model model, HttpServletRequest request) {
        model.addAttribute("departments", departmentService.getAll());
        model.addAttribute("issues",issueService.getAllIssues());
        model.addAttribute("Alert", request.getParameter("Alert"));
        return "admin/issues";
    }

    @RequestMapping("/admin/issue/{id}")
    public String adminIssuePage(@PathVariable(value = "id") int id, Model model) {
        model.addAttribute("departments", departmentService.getAll());
        model.addAttribute("issue", issueService.getIssue(id));
        List<Issue> issues = issueService.getIssuesChilds(id);
        Map issuesMap = new HashMap<Issue,Integer>();
        for (Issue i : issues) {
            issuesMap.put(i,issueService.getIssuesChilds(i.getId()).size());
        }
        model.addAttribute("issueChildsMap", issuesMap);
        model.addAttribute("parentList",issueService.getParrentList(
                    issueService.getIssue(id).getPid()
        ));

        return "admin/issue";
    }

    @RequestMapping("/admin/issue/add")
    public String adminIssueAddPage(@RequestParam("parentID") int pid, @RequestParam("departmentID") int departmentID, Model model){
        model.addAttribute("departments", departmentService.getAll());
        model.addAttribute("department", departmentService.getByID(departmentID));
        model.addAttribute("parentID", pid);
        model.addAttribute("parentList",issueService.getParrentList(pid));
        return "admin/issue-add";
    }

    @RequestMapping(value = "/admin/issue/add", method = RequestMethod.POST)
    public String adminIssueAdd(Model model, HttpServletRequest request){
        Issue d = new Issue(
                Integer.parseInt(request.getParameter("parentId")),
                request.getParameter("issueName"),
                request.getParameter("issueContent"),
                departmentService.getByID(
                        Integer.parseInt(request.getParameter("issueDepartmentID")))
        );

        Issue i = issueService.addIssue(d);

        return "redirect:/admin/issue/" + i.getId();
    }


    @RequestMapping(value = "/admin/issue/edit/{id}", method = RequestMethod.POST)
    public String adminIssueEdit(@PathVariable(value = "id") int id, HttpServletRequest request) {
        Issue i = issueService.getIssue(id);
        i.setName(request.getParameter("issueName"));
        i.setPid(Integer.parseInt(request.getParameter("parentId")));
        i.setContent(request.getParameter("issueContent"));

        issueService.updateIssue(i);

        return "redirect:/admin/issue/" + i.getId();
    }

    @RequestMapping("/admin/issue/remove/{id}")
    public String adminIssueRemove(@PathVariable(value = "id") int id, HttpServletRequest httpServletRequest, Model model){
        // TODO: move to trash
        model.addAttribute("Alert","Issue " + issueService.getIssue(id).getName() +
                " successfully removed");
        int parentID = issueService.getIssue(id).getPid();
        issueService.deleteIssue(id);
        return "redirect:/admin/issue/"+parentID;
    }

    // Department controller
    @RequestMapping("/department/{slug}")
    public String department(@PathVariable(value = "slug") String slug, Model model) {
        model.addAttribute("departments", departmentService.getAll());
        model.addAttribute("department", departmentService.getBySlug(slug));
        model.addAttribute("issues", departmentService.getBySlug(slug).getIssues()
                .stream().filter(issue -> issue.getPid() == 0)
                .collect(Collectors.toList()));
        return "department";
    }

    @RequestMapping("/edit/department/{id}")
    public String editDepartment(@PathVariable(value = "id") int id, HttpServletRequest request) {
        Department d = departmentService.getByID(id);
        d.setName(request.getParameter("departmentName"));
        d.setSlug(request.getParameter("departmentSlug"));
        d.setDescription(request.getParameter("departmentDescription"));

        departmentService.update(d);

        return "redirect:/department/" + request.getParameter("departmentSlug");
    }

    // Issue controller
    @RequestMapping("/issue/{id}")
    public String issue(@PathVariable(value = "id") int id, Model model) {
        Issue issue = new Issue(
                0,
                "404",
                "No issue for this request.",
                new Department("Main", "page", "../"));
        if (issueService.getIssue(id) != null)
            issue = issueService.getIssue(id);

        model.addAttribute("issue", issue);
        model.addAttribute("issueParent",
                issueService.getIssue(issue.getPid()));
        model.addAttribute("parentList", issueService.getParrentList(issue.getPid()));
        model.addAttribute("issues", issueService.getIssuesChilds(id));
        model.addAttribute("departments", departmentService.getAll());

        return "issue";
    }

    @RequestMapping("/add/issue/{slug}")
    public String addIssue(@PathVariable(value = "slug") String slug, HttpServletRequest request) {
        int sPid = Integer.parseInt(request.getParameter("parentId"));
        issueService.addIssue(
                new Issue(
                        sPid,
                        request.getParameter("issueName"),
                        request.getParameter("issueContent"),
                        departmentService.getBySlug(slug)
                ));
        if (sPid>0) return "redirect:/issue/" + sPid;
        return "redirect:/department/" + slug;
    }

    @RequestMapping("/edit/issue")
    public String editIssue(HttpServletRequest request) {
        Issue s = issueService.getIssue(Integer.parseInt(request.getParameter("issueId")));
        s.setName(request.getParameter("issueName"));
        s.setPid(Integer.parseInt(request.getParameter("parentId")));
        s.setContent(request.getParameter("issueContent"));

        issueService.updateIssue(s);
        return "redirect:/issue/"+s.getId();
    }

    @RequestMapping("/remove/issue/{id}")
    public String addIssue(@PathVariable(value = "id") int id, HttpServletRequest request, Model model) {
        int issueParentID = issueService.getIssue(id).getPid();
        String departmentSlug = issueService.getIssue(id).getDepartment().getSlug();
        //check removing condition
        if (id == Integer.parseInt(request.getParameter("issueId"))) {
            issueService.deleteIssue(id);
        }
        if(issueParentID==0)
            return "redirect:/department/" + departmentSlug;
        return "redirect:/issue/" + issueParentID;
    }

    //Others
    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String search(HttpServletRequest request, Model model) {
        String pattern = request.getParameter("pattern");
        if (pattern.isEmpty() || pattern.equals(" ")) return "redirect:search";
        model.addAttribute("departments", departmentService.getAll());
        model.addAttribute("pattern", pattern);
        List<Issue> issues = issueService.searchIssues(pattern);
        for (Issue s : issues) {
            s.setContent(
                    s.getContent()
                            .replaceAll("(?i)("+Pattern.quote(pattern)+")",
                                    "<b>$1</b>"));
            if (s.getContent().length() > 150) {
                s.setContent(
                        s.getContent().substring(0, 150).concat(" ...")
                );
            }
            s.setName(
                    s.getName().replaceAll("(?i)("+Pattern.quote(pattern)+")", "<b>$1</b>")
            );
        }
        model.addAttribute("issues", issues);
        return "search";
    }

    @RequestMapping("/search")
    public String searchEmpty(Model model) {
        model.addAttribute("departments", departmentService.getAll());
        return "search";
    }

    /*----- Redirecting for not mapped pages -----*/
    @RequestMapping("/{page}")
    public String nonMappedPages(@PathVariable(value = "page") String page) {
        System.err.println("--- >>> Requested: " + page);
        return page;
    }

    @RequestMapping("/admin/{page}")
    public String adminPages(@PathVariable(value = "page") String page) {
        System.err.println("--- >>> Requested: /admin/" + page);
        return "admin/" + page;
    }

    @RequestMapping("/test/{page}")
    public String testPages(@PathVariable(value = "page") String page) {
        System.err.println("--- >>> Requested: /test/" + page);
        return "test/" + page;
    }
}
