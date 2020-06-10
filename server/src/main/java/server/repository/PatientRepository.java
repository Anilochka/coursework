package server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import server.entity.Patient;
import server.entity.Ward;

public interface PatientRepository extends CrudRepository<Patient, Long>, JpaSpecificationExecutor<Patient> {
    long countByWard(Ward ward);
}
