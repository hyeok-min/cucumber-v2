package cucumber.rediss.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String name;        //이름
    private String password;    //비밀번호
    private String nickname;    //닉네임
    private String email;       //이메일(아이디)

    private String provider;
    private String providerId;
    private Long oauthCode;
    @Builder    //
    protected Member(String name, String password, String nickname, String email,String provider,String providerId,Long oauthCode) {
        this.name = name;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
    this.provider=provider;
    this.providerId=providerId;
    this.oauthCode=oauthCode;
    }

//==================수정
    public void updateName(String name){ this.name = name; }
    public void updateNickName(String nickname){ this.nickname = nickname; }
    public void updateOauthCode(Long oauthCode){ this.oauthCode = oauthCode; }
    public void updatePassword(PasswordEncoder passwordEncoder, String password){ this.password = passwordEncoder.encode(password); }
}
