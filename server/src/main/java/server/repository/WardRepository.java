package server.repository;

import org.springframework.data.repository.CrudRepository;
import server.entity.Ward;

public interface WardRepository extends CrudRepository<Ward, Long> {
    Iterable<Ward> findAllByNameContainsOrderByNameAsc(String name);
}
