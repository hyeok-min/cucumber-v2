package cucumber.rediss.service;

import cucumber.rediss.domain.Board;
import cucumber.rediss.domain.Category;
import cucumber.rediss.dto.BoardDto;
import cucumber.rediss.repository.BoardRepository;
import cucumber.rediss.repository.RredisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;
    private final RredisRepository rredisRepository;
    private final RredisService rredisService;
    public Board findBoard(Long id){
        return boardRepository.findById(id).get(); }

    public Page<Board> findSearchBoards(Pageable pageable,String searchText,Category category){
        Page<Board> findBoards=boardRepository.search_test(searchText,searchText,category,pageable);
        return findBoards; }

    public Page<Board> findBoardByCategory(Category category, Pageable pageable) {
        return boardRepository.findByCategory(category,pageable); }

    public List<BoardDto> findTopBoard() {
        List<Board> topBoardEntityList = boardRepository.findTop10ByOrderByCountDesc();
        List<BoardDto> topBoardList = new ArrayList<>();
        int i = 1;
        for (Board board : topBoardEntityList) {
            BoardDto boardDto = BoardDto.builder()
                    .topNum("TOP" + i++)
                    .category(board.getCategory().getTr_num())
                    .id(board.getId())
                    .title(board.getTitle())
                    .writer(board.getWriter())
                    .createTime(board.getCreateDate())
                    .count(board.getCount())
                    .build();
            topBoardList.add(boardDto);
        }
        return topBoardList;
    }

    @Transactional
    public void createBoard(Category category,BoardDto boardDto, MultipartFile file, String nickname) throws IOException {
        boardDto.setCreateTime(LocalDateTime.now());

        if(file.isEmpty()) {
            Board board = Board.builder()
                    .title(boardDto.getTitle())
                    .writer(nickname)
                    .content(boardDto.getContent())
                    .createDate(boardDto.getCreateTime())
                    .count(boardDto.getCount())
                    .category(category)
                    .edit_count(boardDto.getEdit_count())
                    .filename(boardDto.setFilename(null))
                    .filepath(boardDto.setFilepath(null))
                    .build();
            boardRepository.save(board);
        }else {
//=======================파일업로드======================================================================
            String filepath = System.getProperty("user.dir") + "/src/main/resources/static/files";
            UUID uuid = UUID.randomUUID();    //랜덤으로 이름생성
            String filename = uuid + "_" + file.getOriginalFilename();    //파일이름은 uuid의 있는 랜덤값+원래이름값
            File saveFile = new File(filepath, filename);
            file.transferTo(saveFile);
//======================================================================================================
            Board board = Board.builder()
                    .title(boardDto.getTitle())
                    .writer(nickname)
                    .content(boardDto.getContent())
                    .createDate(boardDto.getCreateTime())
                    .count(boardDto.getCount())
                    .category(category)
                    .edit_count(boardDto.getEdit_count())
                    .filename(boardDto.setFilename(filename))
                    .filepath(boardDto.setFilepath("/files/" + filename))
                    .build();
            boardRepository.save(board); }
    }

    @Transactional
    @Cacheable(key = "#id",value = "user")
    public Board detailBoard(Long id){
        Board board =boardRepository.findById(id).get();
        if(rredisService.isUserInter(id)){
            return board;
        }

        board.setCount(board.getCount()+1);
        boardRepository.save(board);
        return board; }

    @Transactional
    public void deleteBoard(long id){
        Board board =boardRepository.findById(id).get();
        boardRepository.deleteById(board.getId()); }

    @Transactional
    public void updateBoard(long boardId,BoardDto boardDto, MultipartFile file) throws IOException {   //더티체킹 이용
        Board board =boardRepository.findById(boardId).get();
        board.updateCreateDate(LocalDateTime.now());    //수정시간
        board.setEdit_count(boardDto.getEdit_count()+1);
        board.updateTitle(boardDto.getTitle());
        board.updateContent(boardDto.getContent());

        if(file.isEmpty()){
            board.updateFilename(boardDto.setFilename(null));
            board.updateFilepath(boardDto.setFilepath(null));
        }else{
            String filepath = System.getProperty("user.dir") + "/src/main/resources/static/files";
            UUID uuid = UUID.randomUUID();    //랜덤으로 이름생성
            String filename = uuid + "_" + file.getOriginalFilename();    //파일이름은 uuid의 있는 랜덤값+원래이름값
            File saveFile = new File(filepath, filename);
            file.transferTo(saveFile);

            board.updateFilename(boardDto.setFilename(filename));
            board.updateFilepath(boardDto.setFilepath("/files/" + filename)); }
    }

//================init board=====================================
//    @PostConstruct
//    public void initboard(){
//        for(int i=1; i<21; i++) {
//            Board board = Board.builder()
//                    .title("첫 게시물")
//                    .writer("사용자"+i)
//                    .content("안녕하세요")
//                    .createDate(LocalDateTime.now())
//                    .count((long) 21-i)
//                    .edit_count(0L)
//                    .category(Category.valueOf("num"+i))
//                    .build();
//            boardRepository.save(board);
//        }
//    }
}
