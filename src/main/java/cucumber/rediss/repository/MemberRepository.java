package cucumber.rediss.repository;

import cucumber.rediss.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;



public interface MemberRepository extends JpaRepository<Member,Long> {
    Member findByNickname(String nickname);
    Member findByEmail(String email);
    Member findByName(String name);
}
