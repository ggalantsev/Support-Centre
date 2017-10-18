package ggalantsev.Entity;

import lombok.Data;

@Data
public class DepartmentJSON {

    private int id;

    private String name;

    private String description;

    private String slug;

    private int issuesCount;

    public DepartmentJSON(Department department) {
        this.id = department.getId();
        this.name = department.getName();
        this.description = department.getDescription();
        this.slug = department.getSlug();
        this.issuesCount = department.getIssues().size();
    }
}