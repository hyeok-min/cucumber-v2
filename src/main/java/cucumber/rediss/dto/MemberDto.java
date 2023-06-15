package cucumber.rediss.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class MemberDto {
    @NotEmpty(message="회원이름은 필수입니다")
    private String name;

    @NotEmpty(message = "비밀번호는 필수입니다")
    private String password;

    @NotEmpty(message = "닉네임은 필수입니다")
    @Size(min = 2, max = 10, message = "닉네임은 2자 이상 8자 이하로 입력해주세요.")
    private String nickname;

    @NotEmpty(message = "이메일은 필수입니다")
    @Email(message = "올바른 이메일 주소를 입력해주세요")
    private String email;

    private String pw;
}
