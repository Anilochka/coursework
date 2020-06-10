package server.repository;

import org.springframework.data.repository.CrudRepository;
import server.entity.Diagnosis;

public interface DiagnosisRepository extends CrudRepository<Diagnosis, Long> {
    Iterable<Diagnosis> findAllByNameContainsOrderByNameAsc(String name);
}
