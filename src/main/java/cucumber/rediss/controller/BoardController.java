package cucumber.rediss.controller;

import cucumber.rediss.domain.Board;
import cucumber.rediss.domain.Category;
import cucumber.rediss.dto.BoardDto;
import cucumber.rediss.dto.CommentDto;
import cucumber.rediss.dto.CommunityDto;
import cucumber.rediss.service.BoardService;
import cucumber.rediss.service.CommentService;
import cucumber.rediss.service.CommunityService;
import cucumber.rediss.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;
    private final MemberService memberService;
    private final CommentService commentService;
    private final CommunityService communityService;

 @GetMapping("/board/category/{num}")
public String listBoard(Model model, @PathVariable("num") Category num, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable
        ,Principal principal,String searchText){

    model.addAttribute("category",num.getTr_num());
    model.addAttribute("categoryNum",num.toString());
    log.info("num.getTr_num={}  num.toString={}",num.getTr_num(),num.toString());
    model.addAttribute("member",memberService.loadMemberByNickname(principal)); //로그인사용자표시

     Page<Board> boardAll=null;
    if(searchText ==null) { boardAll=boardService.findBoardByCategory(num, pageable); }
    else{ boardAll=boardService.findSearchBoards(pageable,searchText,num); }

    model.addAttribute("boardAll",boardAll);

    //==========================페이징=====================================================
    int startPage = Math.max(1, boardAll.getPageable().getPageNumber() - 1);
    int endPage = Math.min(boardAll.getTotalPages(), boardAll.getPageable().getPageNumber() + 3);

    if(endPage==0){ endPage = 1; }

    model.addAttribute("startPage", startPage);
    model.addAttribute("endPage", endPage);
    return "Board/listBoard"; }

//==================================================================================================================
    //글 생성
    @GetMapping("/board/category/insert/{numInsertG}")
    public String createBoardForm(@PathVariable("numInsertG") Category numInsertG,Principal principal,Model model) {
        model.addAttribute("category",numInsertG.getTr_num());
        model.addAttribute("member",memberService.loadMemberByNickname(principal)); //로그인사용자표시
        model.addAttribute("boardDto",new BoardDto());
        return "Board/insertBoard";}

    @PostMapping("/board/category/insert/{numInsertP}")
    public String createBoard(@PathVariable("numInsertP") Category numInsertP,Principal principal, BoardDto boardDto, MultipartFile file,Model model) throws Exception {
        if(boardDto.getTitle().length()>=21) {
            model.addAttribute("message", "제목 글자수가 초과했습니다. 다시 입력해주세요.");
            model.addAttribute("searchUrl", "/board/category/insert/" + numInsertP);
            return "message";
        }else if(boardDto.getTitle().length()==0){
            model.addAttribute("message", "제목은 필수입니다. 다시 입력해주세요.");
            model.addAttribute("searchUrl", "/board/category/insert/" + numInsertP);
        return "message";}

        boardService.createBoard(numInsertP,boardDto,file,memberService.loadMemberByNickname(principal)); //로그인사용자표시 & 글작성
        model.addAttribute("message","글작성이 완료되었습니다");
        model.addAttribute("searchUrl","/board/category/"+numInsertP);
        return "message"; }
//==================================================================================================================
    //글 상세
    @GetMapping("/board/detail/{boardId}")
    public String detailBoardForm(@PathVariable("boardId") Long boardId,Model model,Principal principal) {

        model.addAttribute("category",boardService.findBoard(boardId).getCategory().getTr_num());
        model.addAttribute("member",memberService.loadMemberByNickname(principal)); //로그인사용자표시
        model.addAttribute("boardDetail",boardService.detailBoard(boardId));

        //댓글===========================================================
        model.addAttribute("commentsize",commentService.findComment(boardId).size());  //다른방법생각
        model.addAttribute("commentAll",commentService.findComment(boardId));
        model.addAttribute("comment",new CommentDto());
        return "Board/detailBoard"; }
    //댓글==========================================
    @PostMapping("/board/detail/{boardComment}")
    public String detailBoardCreateComment(@PathVariable("boardComment") Long boardComment,Principal principal,Model model,CommentDto commentDto){

        if(commentDto.getComment().length()>=50){   //글자수 제한
            model.addAttribute("message","글자수가 초과했습니다. 다시 입력해주세요.");
            model.addAttribute("searchUrl","/board/detail/"+boardComment);
            return "message";
        }
        commentService.createComment(commentDto,memberService.loadMemberByNickname(principal),boardService.findBoard(boardComment));//
        model.addAttribute("message","댓글작성이 완료되었습니다");
        model.addAttribute("searchUrl","/board/detail/"+boardComment);
        return "message";}

    //댓글삭제
    @GetMapping("/board/comment/delete/{commentId}")
    public String deleteComment(@PathVariable("commentId") long commentId,Model model){

        Long boardId = commentService.findBoardByComment(commentId);

        commentService.deleteComment(commentId);
        model.addAttribute("message","댓글삭제가 완료되었습니다");
        model.addAttribute("searchUrl","/board/detail/"+boardId);
        return "message"; }


    @GetMapping("/board/comment/update/{commentId}")    //모달창 생각해봄
    public String updateComment(@PathVariable("commentId") long commentId,Model model){

        model.addAttribute("message","댓글수정이 완료되었습니다");
        model.addAttribute("searchUrl","/board/test");
        return "message"; }

//    ==================================================================================================================
    //글 삭제
    @GetMapping("/board/delete/{boardId}")
    public String deleteBoard(@PathVariable("boardId") long boardId,Model model){
        Category category=boardService.findBoard(boardId).getCategory();

        boardService.deleteBoard(boardId);
        model.addAttribute("message","삭제가 완료되었습니다");
        model.addAttribute("searchUrl","/board/category/"+category);
        return "message"; }
//    ==================================================================================================================
    //글 수정
    @GetMapping("/board/edit/{boardId}")
    public String editBoardForm(@PathVariable("boardId") long boardId,Model model,Principal principal){
        model.addAttribute("member",memberService.loadMemberByNickname(principal)); //로그인사용자표시
        model.addAttribute("boardEdit",boardService.findBoard(boardId));
        return "Board/editBoard"; }

    @PostMapping("/board/edit/{boardId}")
    public String editBoard(@PathVariable("boardId") long boardId,@ModelAttribute("boardEdit") BoardDto boardEdit, MultipartFile file,Model model) throws IOException {
        if(boardEdit.getTitle().length()>=21) {
            model.addAttribute("message", "제목 글자수가 초과했습니다. 다시 입력해주세요.");
            model.addAttribute("searchUrl", "/board/edit/" + boardId);
            return "message";
        }else if(boardEdit.getTitle().length()==0){
            model.addAttribute("message", "제목은 필수입니다. 다시 입력해주세요.");
            model.addAttribute("searchUrl", "/board/edit/" + boardId);
            return "message";}

        log.info("======update board==={}",boardEdit);
        boardService.updateBoard(boardId,boardEdit,file);
        model.addAttribute("message","수정이 완료되었습니다");
        model.addAttribute("searchUrl","/board/detail/"+boardId);
        return "message"; }
//    ==================================================================================================================
    //커뮤니티
    @GetMapping("/community")
    public String listCommunity(Principal principal, Model model){
        model.addAttribute("member", memberService.loadMemberByNickname(principal));
        model.addAttribute("communityAll",communityService.findCommunityList());
        model.addAttribute("community",new CommunityDto());
        return "Board/communityBoard"; }

    @PostMapping("/community")
    public String createCommunity(CommunityDto communityDto,Model model,Principal principal){
     communityService.createCommunity(communityDto,memberService.loadMemberByNickname(principal));
        model.addAttribute("message","커뮤니티 댓글을 작성하셨습니다.");
        model.addAttribute("searchUrl","/community");
        return "message";
    }
    @GetMapping("/community/{communityId}")
    public String deleteCommunityContent(@PathVariable("communityId") Long communityId,Model model){
        Long community = communityService.findCommunity(communityId).getId();
        communityService.deleteCommunity(community);

        model.addAttribute("message","커뮤니티 댓글이 삭제되었습니다");
        model.addAttribute("searchUrl","/community");
        return "message";
    }
}

