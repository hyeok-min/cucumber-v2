package cucumber.rediss.repository;

import cucumber.rediss.domain.Board;
import cucumber.rediss.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findAllByBoard(Board board);
}
