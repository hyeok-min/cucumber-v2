package cucumber.rediss.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RredisService {
    private final RedisTemplate<Long,Object> redisTemplate;

    public boolean isUserInter(Long id){
        if(redisTemplate.hasKey(id)){
            return false;
        }
        return true;
    }
}
