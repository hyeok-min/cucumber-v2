package cucumber.rediss.service;

import cucumber.rediss.domain.Community;
import cucumber.rediss.dto.CommunityDto;
import cucumber.rediss.repository.CommunityRepository;
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
public class CommunityService {
    private final CommunityRepository communityRepository;

    public Community findCommunity(Long id){
        return communityRepository.findById(id).get();
    }
    public List<Community> findCommunityList(){
        return communityRepository.findAll();
    }

    @Transactional
    public void createCommunity(CommunityDto communityDto,String name){
        communityDto.setCreateTime(LocalDateTime.now());

        Community community=Community.builder()
                .createDate(communityDto.getCreateTime())
                .writer(name)
                .content(communityDto.getContent())
                .build();
        communityRepository.save(community); }

    @Transactional
    public void deleteCommunity(Long id){ communityRepository.deleteById(id); }
}
