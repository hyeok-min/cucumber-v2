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


    @Override   //loadUserByUsername의 함수에서 매개변수인 userEmail은 html일 경우 name="userEmail" 과 동일해야 한다
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        log.info("securityservice in");
        Member findMember=memberRepository.findByEmail(userEmail);
        if(findMember!=null)
        return new LoginSecurityMember(findMember); //시큐리티의 세션에 유저정보가 저장이됨.

        return null;
    }
}
