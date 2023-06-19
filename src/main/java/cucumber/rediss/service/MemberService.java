package cucumber.rediss.service;

import cucumber.rediss.domain.Member;
import cucumber.rediss.dto.MemberDto;
import cucumber.rediss.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final BCryptPasswordEncoder encoder;    //비밀번호 암호화
    private final MemberRepository memberRepository;

    //회원가입
    @Transactional
    public void memberJoin(MemberDto memberDto){
        Member member=Member.builder()
                .name(memberDto.getName())
                .nickname(memberDto.getNickname())
                .email(memberDto.getEmail())
                .oauthCode(0L)
                .password(encoder.encode(memberDto.getPassword()))
                .build();

        memberRepository.save(member); }

    //회원변경을 위한 비밀번호 인증로직
    public Boolean isEditAuth(Principal principal,String pw){
      return encoder.matches(pw, findMemberByEmail(principal).getPassword()); }

    //회원정보가져오기
    public Member loadMemberInfoByNickname(Principal principal){
        return findMemberByEmail(principal); }

    //더티체킹을 이용해서 회원변경
    @Transactional
    public void memberUpdate(MemberDto memberDto,Principal principal){
        Member member = findMemberByEmail(principal);
        member.updateNickName(memberDto.getNickname());
        member.updatePassword(encoder,memberDto.getPassword());
        member.updateName(memberDto.getName());
        member.updateOauthCode(0L);}

    //회원삭제
    @Transactional
    public void memberDelete(Principal principal){
        Member member = findMemberByEmail(principal);
        memberRepository.deleteById(member.getId()); }
//===========================================================================================================
    //이메일 중복검증
    public Member validateByEmail(MemberDto memberDto){
        String valid_name=memberDto.getEmail();
        return memberRepository.findByEmail(valid_name); }

    //닉네임 중복검증
    public Member validateByNick(MemberDto memberDto){
        String valid_name=memberDto.getNickname();
        return memberRepository.findByNickname(valid_name); }

    //로그인 사용자 정보 표시 메서드
    @Cacheable(key = "#principal.getName()",value = "enterUser")
    public String loadMemberByNickname(Principal principal){
        return findMemberByEmail(principal).getNickname(); }

    //현재 접속한 유저를 이메일로 모든정보가져오기
    public Member findMemberByEmail(Principal principal){
        String email= principal.getName();
        return memberRepository.findByEmail(email); }
}

