package ggalantsev.controller;

import ggalantsev.Entity.Department;
import ggalantsev.Entity.Issue;
import ggalantsev.Service.DepartmentService;
import ggalantsev.Service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
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

    @RequestMapping("/admin")
    public String adminPages() {
        return "admin/index" ;
    }

    // Department page
    @RequestMapping("/department/{slug}")
    public String department(@PathVariable(value = "slug") String slug, Model model) {
        model.addAttribute("departments", departmentService.getAll());
        model.addAttribute("department", departmentService.getBySlug(slug));
        model.addAttribute("issues", departmentService.getBySlug(slug).getIssues()
                .stream().filter(issue -> issue.getPid() == 0)
                .collect(Collectors.toList()));
        return "department";
    }

    @RequestMapping("/edit/department/{slug}")
    public String editDepartment(@PathVariable(value = "slug") String slug, HttpServletRequest request) {
        Department d = departmentService.getBySlug(slug);
        d.setName(request.getParameter("departmentName"));
        d.setSlug(request.getParameter("departmentSlug"));
        d.setDescription(request.getParameter("departmentDescription"));

        departmentService.update(d);

        return "redirect:/department/" + request.getParameter("departmentSlug");
    }

    // Issue page
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
        model.addAttribute("parentList",issueService.getParrentList(issue.getPid()));
        model.addAttribute("issues", issueService.getIssuesChilds(id));
        model.addAttribute("departments", departmentService.getAll());

        return "issue";
    }

    @RequestMapping("/add/issue/{slug}")
    public String addIssue(@PathVariable(value = "slug") String slug, HttpServletRequest request) {
        String sName = request.getParameter("issueName");
        int sPid = Integer.parseInt(request.getParameter("parentId"));
        String sContent = request.getParameter("issueContent");

        issueService.addIssue(
                new Issue(
                        sPid,
                        sName,
                        sContent,
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
        //No way to change department for now.

        issueService.updateIssue(s);
        return "redirect:/issue/"+s.getId();
    }

    @RequestMapping("/remove/issue/{id}")
    public String addIssue(@PathVariable(value = "id") int id, HttpServletRequest request, Model model) {
        int issueParentID=0;
        String departmenntSlug = "";
        if (id == Integer.parseInt(request.getParameter("issueId"))) {
            issueParentID = issueService.getIssue(id).getPid();
            departmenntSlug = issueService.getIssue(id).getDepartment().getSlug();
            issueService.deleteIssue(id);
        }
        if(issueParentID==0)
            return "redirect:/department/" + departmenntSlug;
        return "redirect:/issue/" + issueParentID;
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String search(HttpServletRequest request, Model model) {
        String pattern = request.getParameter("pattern");
        if (pattern == "" || pattern == " ") return "redirect:search";
        model.addAttribute("pattern", pattern);
        model.addAttribute("departments", departmentService.getAll());
        List<Issue> issues = issueService.searchIssues(pattern);
        for (Issue s : issues) {
            s.setContent(
                    s.getContent().replaceAll("(?i)("+Pattern.quote(pattern)+")", "<b>$1</b>")
            );
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
