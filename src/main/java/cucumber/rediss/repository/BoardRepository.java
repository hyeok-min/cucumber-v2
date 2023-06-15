package cucumber.rediss.repository;

import cucumber.rediss.domain.Board;
import cucumber.rediss.domain.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface BoardRepository extends JpaRepository<Board,Long> {

    @Query(value = "select b from Board b where (b.content like %:content% or b.title like %:title%) and b.category= :category")
    Page<Board> search_test(@Param("content") String content,
                            @Param("title") String title,
                            @Param("category") Category category,
                            Pageable pageable);

    List<Board> findTop10ByOrderByCountDesc();

    Page<Board> findByCategory(Category category, Pageable pageable);

}

