package cucumber.rediss.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Board {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private Category category;

    @JsonIgnore
    @OneToMany(mappedBy = "board",cascade = CascadeType.REMOVE) //게시글삭제시 댓글도 삭제
    @OrderBy("id asc")  //댓글정렬
    private List<Comment> comment=new ArrayList<>();

    private String title;   //제목
    private String writer;  //작성자
    private String content; //내용

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createDate;   //게시판,댓글 생성시간
    private Long count; //조회수
    private Long edit_count;    //수정count
    private String filename;
    private String filepath;

    @Builder
    protected Board(String title,String writer,String content,LocalDateTime createDate,Long count,Category category,String filepath,String filename, Long edit_count){
        this.title=title;
        this.writer=writer;
        this.content=content;
        this.createDate=createDate;
        this.count=count;
        this.category=category;
        this.filename=filename;
        this.filepath=filepath;
        this.edit_count=edit_count;
    }
    public void setCount(Long count) {
        this.count = count;
    }


//    =================수정=====================
    public void setEdit_count(Long edit_count){this.edit_count=edit_count;}
    public void updateTitle(String title){this.title=title;}
    public void updateContent(String content){this.content=content;}
    public void updateFilename(String filename){this.filename=filename;}
    public void updateFilepath(String filepath){this.filepath=filepath;}
    public void updateCreateDate(LocalDateTime createDate){this.createDate=createDate;}

}
