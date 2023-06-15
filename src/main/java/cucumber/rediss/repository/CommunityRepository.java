package cucumber.rediss.repository;

import cucumber.rediss.domain.Community;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityRepository extends JpaRepository<Community,Long> {

}
