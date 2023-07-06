package cucumber.rediss.springsecurity;

import cucumber.rediss.domain.Member;
import cucumber.rediss.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginSecurityService implements UserDetailsService {

    private final MemberRepository memberRepository;


    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        log.info("securityservice in");
        Member findMember=memberRepository.findByEmail(userEmail);
        if(findMember!=null)
        return new LoginSecurityMember(findMember);

        return null;
    }
}
