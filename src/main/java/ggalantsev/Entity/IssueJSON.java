package ggalantsev.Entity;

import lombok.Data;

@Data
public class IssueJSON {

    private int id;

    private int parentID;

    private String name;

    private String content;

    private String departmentName;

    public IssueJSON(Issue issue) {
        this.id = issue.getId();
        this.parentID = issue.getPid();
        this.name = issue.getName();
        this.content = issue.getContent();
        this.departmentName = issue.getDepartment().getName();
    }
}
