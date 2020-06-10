package server.entity;

import org.hibernate.annotations.Formula;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Entity
@Table(name = "wards")
public class Ward {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(length = 50, unique = true, nullable = false)
    @Size(max = 50, message = "Ward name cant'be longer than 50 characters")
    private String name;

    @Min(value = 1, message = "Max count must be positive")
    private long maxCount;

    @Formula("(SELECT COUNT(*) From people o WHERE o.ward_id = id)")
    private long patientCount;

    public Ward() {
    }

    public Ward(String name, long maxCount) {
        this.name = name;
        this.maxCount = maxCount;
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

    public long getMaxCount() {
        return maxCount;
    }

    public long getPatientCount() {
        return patientCount;
    }
}
