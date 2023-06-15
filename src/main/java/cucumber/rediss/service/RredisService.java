package cucumber.rediss.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class RredisService {
    private final RedisTemplate<String,Object> redisTemplate;

    public boolean isUserEnter(Long id){
        log.info("====isUserEnter in======");
        if(redisTemplate.hasKey(String.valueOf(id))){
            log.info("====isUserEnter HASKEY in======");
            return true;
        }
        return false;
    }
}
