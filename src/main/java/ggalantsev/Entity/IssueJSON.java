package ggalantsev.Entity;

public class IssueJSON {

    private int id;

    private int parentID;

    private String name;

    private String content;

    private String departmentName;

    public IssueJSON() {
    }

    public IssueJSON(Issue issue) {
        this.id = issue.getId();
        this.parentID = issue.getPid();
        this.name = issue.getName();
        this.content = issue.getContent();
        this.departmentName = issue.getDepartment().getName();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentID() {
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    @Override
    public String toString() {
        return "IssueJSON{" +
                "id=" + id +
                ", parentID=" + parentID +
                ", name='" + name + '\'' +
                ", departmentName='" + departmentName + '\'' +
                '}';
    }
}
