package ggalantsev.controller;

import ggalantsev.Entity.Department;
import ggalantsev.Entity.Issue;
import ggalantsev.Entity.User;
import ggalantsev.Entity.UserRole;
import ggalantsev.Service.DepartmentService;
import ggalantsev.Service.IssueService;
import ggalantsev.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@org.springframework.stereotype.Controller
public class Controller {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IssueService issueService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public String main(Model model) {
        model.addAttribute("departments", departmentService.getAll());
        return "index";
    }

    //Admin pages controller
    @RequestMapping("/admin")
    public String adminPage(Model model) {
        model.addAttribute("departments", departmentService.getAll());
        return "admin/index";
    }

    @RequestMapping(value = "/admin/departments", method = RequestMethod.GET)
    public String adminDepartmentsPage(Model model, HttpServletRequest request) {
        model.addAttribute("departments", departmentService.getAll());
        return "/admin/departments";
    }

    @RequestMapping("/admin/department/{slug}")
    public String adminDepartmentPage(@PathVariable(value = "slug") String slug, Model model) {
        model.addAttribute("departments", departmentService.getAll());
        model.addAttribute("department", departmentService.getBySlug(slug));
        List<Issue> issues = departmentService.getBySlug(slug).getIssues()
                .stream().filter(issue -> issue.getPid() == 0)
                .collect(Collectors.toList());
        Map issuesMap = new HashMap<Issue, Integer>();
        for (Issue i : issues) {
            issuesMap.put(i, issueService.getIssuesChilds(i.getId()).size());
        }
        model.addAttribute("issues", issuesMap);
        return "admin/department";
    }

    @RequestMapping("/admin/department/add")
    public String adminDepartmentAddPage(Model model) {
        model.addAttribute("departments", departmentService.getAll());
        return "admin/department-add";
    }

    @RequestMapping(value = "/admin/department/add", method = RequestMethod.POST)
    public String adminDepartmentAdd(Model model, HttpServletRequest request) {
        if (departmentService.getBySlug(request.getParameter("departmentSlug")) != null) {
            model.addAttribute("departments", departmentService.getAll());
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
            return "redirect:/admin/department/" + d.getSlug();
        }
    }

    @RequestMapping(value = "/admin/department/edit/{id}", method = RequestMethod.POST)
    public String adminDepartmentEdit(@PathVariable(value = "id") int id, HttpServletRequest request) {
        Department d = departmentService.getByID(id);
        d.setName(request.getParameter("departmentName"));
        d.setSlug(request.getParameter("departmentSlug"));
        d.setDescription(request.getParameter("departmentDescription"));

        departmentService.update(d);

        return "redirect:/admin/department/" + d.getSlug();
    }

    @RequestMapping("/admin/department/remove/{id}")
    public ModelAndView adminDepartmentRemove(@PathVariable(value = "id") int id, RedirectAttributes redir, Model model) {
        redir.addFlashAttribute("Alert", "Department <b>" + departmentService.getByID(id).getName() +
                "</b> successfully removed");
        model.addAttribute("departments", departmentService.getAll());
        departmentService.delete(id);
        return new ModelAndView("redirect:/admin/departments");
    }

    @RequestMapping("/admin/issues")
    public String adminIssuesPage(Model model, HttpServletRequest request) {
        model.addAttribute("departments", departmentService.getAll());
        model.addAttribute("issues", issueService.getAllIssues());
        model.addAttribute("Alert", request.getParameter("Alert"));
        return "admin/issues";
    }

    @RequestMapping("/admin/issue/{id}")
    public String adminIssuePage(@PathVariable(value = "id") int id, Model model) {
        model.addAttribute("departments", departmentService.getAll());
        model.addAttribute("issue", issueService.getIssue(id));
        List<Issue> issues = issueService.getIssuesChilds(id);
        Map issuesMap = new HashMap<Issue, Integer>();
        for (Issue i : issues) {
            issuesMap.put(i, issueService.getIssuesChilds(i.getId()).size());
        }
        model.addAttribute("issueChildsMap", issuesMap);
        model.addAttribute("parentList", issueService.getParrentList(
                issueService.getIssue(id).getPid()
        ));

        return "admin/issue";
    }

    @RequestMapping("/admin/issue/add")
    public String adminIssueAddPage(@RequestParam("parentID") int pid, @RequestParam("departmentID") int departmentID, Model model) {
        model.addAttribute("departments", departmentService.getAll());
        model.addAttribute("department", departmentService.getByID(departmentID));
        model.addAttribute("parentID", pid);
        model.addAttribute("parentList", issueService.getParrentList(pid));
        return "admin/issue-add";
    }

    @RequestMapping(value = "/admin/issue/add", method = RequestMethod.POST)
    public String adminIssueAdd(Model model, HttpServletRequest request) {
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
    public String adminIssueRemove(@PathVariable(value = "id") int id, HttpServletRequest httpServletRequest, Model model) {
        // TODO: move to trash
        model.addAttribute("Alert", "Issue " + issueService.getIssue(id).getName() +
                " successfully removed");
        int parentID = issueService.getIssue(id).getPid();
        issueService.deleteIssue(id);
        return "redirect:/admin/issue/" + parentID;
    }

    @RequestMapping("/admin/users")
    public String adminUsersPage(Model model) {
        model.addAttribute("departments", departmentService.getAll());
        model.addAttribute("users", userService.getAll());
        return "admin/users";
    }

    @RequestMapping("/admin/users/test")
    public ModelAndView usertest(Model model, RedirectAttributes redir) {
        redir.addFlashAttribute("Alert", String.valueOf(123123));
        return new ModelAndView("redirect:/admin/users");
    }

    @RequestMapping("admin/user/{id}")
    public String adminUserPage(@PathVariable(value = "id") int id, Model model, HttpServletRequest request) {
        model.addAttribute("departments", departmentService.getAll());
        model.addAttribute("user", userService.getById(id));
        model.addAttribute("Alert", request.getParameter("Alert"));
        return "admin/user";
    }

    @RequestMapping("admin/user/add")
    public String adminUserAddPage(Model model, HttpServletRequest request) {
        model.addAttribute("departments", departmentService.getAll());
        model.addAttribute("user", new User());
        return "admin/user-add";
    }

    @RequestMapping(value = "admin/user/save", method = RequestMethod.POST)
    public ModelAndView adminUserSave(Model model, HttpServletRequest request, RedirectAttributes redir) {
        User user = new User();
        // filling user fields
        user.setUsername(request.getParameter("username").trim());
        user.setName(request.getParameter("name"));
        user.setSurname(request.getParameter("surname"));
        user.setAuthorities(Arrays.asList(
                request.getParameter("roleUser") != null ? UserRole.ROLE_USER : null,
                request.getParameter("roleAdmin") != null ? UserRole.ROLE_ADMIN : null));
        user.setAccountNonExpired(request.getParameter("accountNonExpired") != null);
        user.setAccountNonLocked(request.getParameter("accountNonLocked") != null);
        user.setCredentialsNonExpired(request.getParameter("credentialsNonExpired") != null);
        user.setEnabled(request.getParameter("enabled") != null);

        model.addAttribute("departments", departmentService.getAll());
        model.addAttribute("user", user);

        if (request.getParameter("id") != null) {
            user.setId(Integer.parseInt(request.getParameter("id")));
        } else if (userService.existsByUsername(user.getUsername())) { // if user existed
            model.addAttribute("Alert", "User with this username already exists");
            return new ModelAndView("/admin/user-add");
        }

        if (request.getParameter("password1").equals(request.getParameter("password2"))) {
            user.setPassword(new BCryptPasswordEncoder()
                    .encode(request.getParameter("password1")));
        } else {
            model.addAttribute("Alert", "Passwords are different");
            if (user.getId() == 0) {
                return new ModelAndView("/admin/user-add");
            } else {
                return new ModelAndView("/admin/user");
            }
        }

        User savedUser = userService.save(user);

        redir.addFlashAttribute("Notification", request.getParameter("Alert") == null ? "User saved" : null);
        return new ModelAndView("redirect:/admin/user/" + savedUser.getId());
    }

    @RequestMapping("/admin/user/remove/{id}")
    public ModelAndView deleteUser(@PathVariable("id") int id, HttpServletResponse response, RedirectAttributes redir) {
        User user = userService.getById(id);
        String username = user.getUsername();
        userService.delete(user);
        redir.addFlashAttribute("Alert", "User <b>" + username + "</b> removed.");
        return new ModelAndView("redirect:/admin/users");
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
        if (sPid > 0) return "redirect:/issue/" + sPid;
        return "redirect:/department/" + slug;
    }

    @RequestMapping("/edit/issue")
    public String editIssue(HttpServletRequest request) {
        Issue s = issueService.getIssue(Integer.parseInt(request.getParameter("issueId")));
        s.setName(request.getParameter("issueName"));
        s.setPid(Integer.parseInt(request.getParameter("parentId")));
        s.setContent(request.getParameter("issueContent"));

        issueService.updateIssue(s);
        return "redirect:/issue/" + s.getId();
    }

    @RequestMapping("/remove/issue/{id}")
    public String addIssue(@PathVariable(value = "id") int id, HttpServletRequest request, Model model) {
        int issueParentID = issueService.getIssue(id).getPid();
        String departmentSlug = issueService.getIssue(id).getDepartment().getSlug();
        //check removing condition
        if (id == Integer.parseInt(request.getParameter("issueId"))) {
            issueService.deleteIssue(id);
        }
        if (issueParentID == 0)
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
                            .replaceAll("(?i)(" + Pattern.quote(pattern) + ")",
                                    "<b>$1</b>"));
            if (s.getContent().length() > 150) {
                s.setContent(
                        s.getContent().substring(0, 150).concat(" ...")
                );
            }
            s.setName(
                    s.getName().replaceAll("(?i)(" + Pattern.quote(pattern) + ")", "<b>$1</b>")
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
