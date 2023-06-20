package cucumber.rediss.springsecurity;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig  extends WebSecurityConfigurerAdapter {

    private final LoginSecurityService loginSecurityService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()

                .authorizeRequests()
                .antMatchers("/init/login", "/member/new").permitAll()  //로그인없이 접근가능
                .anyRequest().authenticated()
                .and()
                .formLogin()//'formLogin()'에서 폼방식 로그인을 사용할 것임을 알림
                .loginPage("/")//커스텀 페이지로 로그인 페이지변경
                .usernameParameter("userEmail")
                .passwordParameter("passWord")
                .loginProcessingUrl("/member/loginPro")//form 태그의 Action URL.
                .defaultSuccessUrl("/home",true)//로그인인증 성공후 갈 페이지
                .failureForwardUrl("/loginFail")
                .permitAll()
                .and()
                .logout().logoutUrl("/member/logout").logoutSuccessUrl("/")
                .and()
                .httpBasic();



    }

    @Override
    public void configure(WebSecurity web) throws Exception {   //화면 깨지지않게 하기
        web.ignoring().antMatchers("/css/**");
        web.ignoring().antMatchers("/js/**");
        web.ignoring().antMatchers("/images/**");
        web.ignoring().antMatchers("/files/**");

    }

    @Bean
    public BCryptPasswordEncoder getpasswordEncoder() { return new BCryptPasswordEncoder(); }

    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(loginSecurityService).passwordEncoder(getpasswordEncoder());
    }

}
