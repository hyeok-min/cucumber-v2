package cucumber.rediss.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommunityDto {
    private String content;
    private String writer;
    private LocalDateTime createTime;
}
