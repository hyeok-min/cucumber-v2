package cucumber.rediss.dto;

import cucumber.rediss.domain.Board;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private String comment;
    private String writer;
    private Board board;
    private LocalDateTime createDate;
}
