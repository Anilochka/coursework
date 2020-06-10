package server.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "people")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(length = 20, nullable = false)
    @Size(max = 20, message = "First name cant'be longer than 20 characters")
    private String firstName;

    @Column(length = 20, nullable = false)
    @Size(max = 20, message = "LAst name cant'be longer than 20 characters")
    private String lastName;

    @Column(length = 20, nullable = false)
    @Size(max = 20, message = "Father name cant'be longer than 20 characters")
    private String fatherName;

    @ManyToOne(targetEntity = Diagnosis.class)
    @NotNull(message = "Diagnosis is mandatory")
    private Diagnosis diagnosis;

    @ManyToOne(targetEntity = Ward.class)
    @NotNull(message = "Ward is mandatory")
    private Ward ward;

    public Patient() {
    }

    public Patient(String firstName, String lastName, String fatherName, Diagnosis diagnosis, Ward ward) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.fatherName = fatherName;
        this.diagnosis = diagnosis;
        this.ward = ward;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public Diagnosis getDiagnosis() {
        return diagnosis;
    }

    public Ward getWard() {
        return ward;
    }
}
