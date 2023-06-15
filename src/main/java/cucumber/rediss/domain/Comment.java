package cucumber.rediss.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="comments")
public class Comment {

    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    private LocalDateTime createDate;


    @Column(name = "comments")
    private String comment; //댓글내용

    private String writer;

    @Builder
    protected Comment(String comment,String writer,LocalDateTime createDate,Board board){
        this.comment=comment;
        this.writer=writer;
        this.createDate=createDate;
        this.board=board; }
}
