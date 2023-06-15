package cucumber.rediss.service;

import cucumber.rediss.domain.Board;
import cucumber.rediss.domain.Comment;
import cucumber.rediss.dto.CommentDto;
import cucumber.rediss.repository.BoardRepository;
import cucumber.rediss.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    public List<Comment> findComment(Long id){
        Board board = boardRepository.findById(id).get();
        return commentRepository.findAllByBoard(board); }

    public Long findBoardByComment(Long id){
        Comment comment =commentRepository.findById(id).get();
        return comment.getBoard().getId(); }

    @Transactional
    public void createComment(CommentDto commentDto, String name,Board boardId){
        commentDto.setCreateDate(LocalDateTime.now());

        Comment comment=Comment.builder()
                .comment(commentDto.getComment())
                .createDate(commentDto.getCreateDate())
                .writer(name)
                .board(boardId)
                .build();
        commentRepository.save(comment); }

    @Transactional
    public void deleteComment(Long id){ commentRepository.deleteById(id);}


    @Transactional
    public void update_comment(Long id){
        Comment comment=commentRepository.findById(id).get();
    }
}
