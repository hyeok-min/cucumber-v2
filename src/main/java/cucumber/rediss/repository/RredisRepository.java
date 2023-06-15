package cucumber.rediss.repository;

import cucumber.rediss.domain.Rredis;
import org.springframework.data.repository.CrudRepository;

public interface RredisRepository extends CrudRepository<Rredis,Long> {

}
