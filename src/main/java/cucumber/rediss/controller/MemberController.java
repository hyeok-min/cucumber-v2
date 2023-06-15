package cucumber.rediss.controller;

import cucumber.rediss.dto.MemberDto;
import cucumber.rediss.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final BCryptPasswordEncoder encoder;    //비밀번호 암호화  //나중에 삭제
    private final MemberService memberService;

    //로그인
    @GetMapping("/")
    public String login(){ return "LoginAndNew/loginMember"; }

    @PostMapping("/loginFail") //로그인실패포워딩할때 POST요청해야함
    public String loginFail(Model model){
        model.addAttribute("message", "로그인실패! 다시 로그인해주세요.");
        model.addAttribute("searchUrl", "/");
        return "message"; }

    //회원가입
    @GetMapping("/member/new")
    public String createMemberForm(Model model) {
        model.addAttribute("newMemberForm", new MemberDto());
        return "LoginAndNew/newMember"; }

    @PostMapping("/member/new")
    public String createMember(@ModelAttribute("newMemberForm") @Valid MemberDto newMemberForm, BindingResult result, Model model) {
        if (result.hasErrors()) {
            log.info("binding result");
            return "LoginAndNew/newMember"; }

        if (memberService.validateByEmail(newMemberForm) == null && memberService.validateByNick(newMemberForm) == null) {
            log.info("====둘다없음====");
            memberService.memberJoin(newMemberForm);
            model.addAttribute("message", "회원가입 성공");
            model.addAttribute("searchUrl", "/");
            return "message";
        } else if (memberService.validateByEmail(newMemberForm) != null && memberService.validateByNick(newMemberForm) != null) {
            log.info("====둘다있음====");
            model.addAttribute("message", "이미 존재하는 이메일과 닉네임 입니다.");
            model.addAttribute("searchUrl", "/member/new");
            return "message";
        } else if (memberService.validateByEmail(newMemberForm) != null) {
            log.info("====이메일만있음====");
            model.addAttribute("message", "이미 존재하는 이메일입니다.");
            model.addAttribute("searchUrl", "/member/new");
            return "message";
        } else{
            log.info("====닉네임만있음====");
            model.addAttribute("message", "이미 존재하는 닉네임입니다.");
            model.addAttribute("searchUrl", "/member/new");
            return "message";
        }
    }

    //회원정보 변경을 위한인증
    @GetMapping("/member/edit-auth")
    public String editMemberAuthForm(Model model,Principal principal){
        model.addAttribute("member",memberService.loadMemberByNickname(principal)); //로그인사용자표시
        model.addAttribute("pw",new MemberDto());
        return "LoginAndNew/editAuthMember"; }

    @PostMapping("/member/edit-auth")
    public String editMemberAuth(Principal principal,MemberDto memberDto,Model model){
        Boolean isResult = memberService.isEditAuth(principal, memberDto.getPw());
        if(!isResult){
            model.addAttribute("message", "비밀번호가 일치하지않습니다.");
            model.addAttribute("searchUrl", "/member/edit-auth");
            return "message"; }

        return "redirect:/member/info"; }

    //회원정보
    @GetMapping("/member/info")
    public String editMemberInfo( Model model,Principal principal){
        model.addAttribute("member",memberService.loadMemberByNickname(principal)); //로그인사용자표시
        model.addAttribute("members",memberService.findMemberByEmail(principal));
        return "LoginAndNew/editInfoMember"; }

    //회원정보변경
    @GetMapping("/member/edit")
    public String editMemberForm(Principal principal,Model model){
        model.addAttribute("member",memberService.loadMemberByNickname(principal)); //로그인사용자표시
        model.addAttribute("editMembers",memberService.loadMemberInfoByNickname(principal));
        return "LoginAndNew/editMember"; }
    
    @PostMapping("/member/edit")
    public String editMember(@ModelAttribute("editMembers") @Valid MemberDto editMembers, BindingResult result,Principal principal,Model model){
        if (result.hasErrors()){
            log.info("binding result");
            return "LoginAndNew/editMember"; }

        if ( memberService.validateByNick(editMembers) == null || memberService.validateByNick(editMembers).getNickname().equals(memberService.loadMemberByNickname(principal))) {
            memberService.memberUpdate(editMembers,principal);    ////변경
            model.addAttribute("message", "회원변경 성공!  다시 로그인해주세요.");
            model.addAttribute("searchUrl", "/member/logout");
            return "message";
        }else{
            log.info("=======fail====");
            model.addAttribute("message", "이미 존재하는 닉네임 입니다.");
            model.addAttribute("searchUrl", "/member/edit/");
            return "message"; }
    }

    //삭제
    @GetMapping("/member/delete")
    public String deleteMember(Principal principal,Model model){
        memberService.memberDelete(principal);
        model.addAttribute("message","회원탈퇴가 완료되었습니다");
        model.addAttribute("searchUrl","/");
        return "message"; }

}
