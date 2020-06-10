package server.entity;

import org.hibernate.annotations.Formula;
import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "diagnosis")
public class Diagnosis {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(length = 50, unique = true, nullable = false)
    @Size(max = 50, message = "Diagnosis name cant'be longer than 50 characters")
    private String name;

    @Formula("(SELECT COUNT(*) From people o WHERE o.diagnosis_id = id)")
    private long patientCount;

    public Diagnosis() {
    }

    public Diagnosis(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPatientCount() {
        return patientCount;
    }
}
