package cucumber.rediss.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@RedisHash("Rredis")
public class Rredis {
    @Id
    private Long id;


}
