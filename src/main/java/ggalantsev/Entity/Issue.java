package ggalantsev.Entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.DatabaseMetaData;
import java.util.Date;

@Entity
@Table(name = "issues")
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @NotNull
    @Column(name = "pid")
    private int pid;

    @NotNull
    @Column(name = "name")
    private String name;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

//    @Temporal(TemporalType.TIMESTAMP)
//    private Date date;

    public Issue() {}

    public Issue(int pid, String name, String content, Department department) {
        this.pid = pid;
        this.name = name;
        this.content = content;
        this.department = department;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
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

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "Issue #" + id +
                ", pid: " + pid +
                ", name: " + name +
                ", content: " + content +
                '.';
    }
}
