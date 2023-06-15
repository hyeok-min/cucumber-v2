package cucumber.rediss.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class BoardDto {
    private Long id;
    private String title;
    private String content;
    private String writer;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createTime;
    private String category;

    private Long count= 0L;
    private Long edit_count=0L;
    private String filename;
    private String filepath;
    private String topNum;

    public String setFilename(String filename) {
        this.filename = filename;
        return filename; }

    public String setFilepath(String filepath) {
        this.filepath = filepath;
        return filepath; }

    @Builder
    public BoardDto(String topNum,String category,Long id,String title,String writer,LocalDateTime createTime,Long count){
       this.topNum=topNum;
       this.category=category;
       this.id=id;
       this.title=title;
       this.writer=writer;
       this.createTime=createTime;
       this.count=count;
    }
}
