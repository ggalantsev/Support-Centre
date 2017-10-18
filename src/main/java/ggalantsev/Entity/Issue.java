package ggalantsev.Entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.DatabaseMetaData;
import java.util.Date;

//lombok
@Getter
@Setter
@ToString

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

    @Column(name = "content", columnDefinition = "text")
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

}
