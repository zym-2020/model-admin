package njnu.edu.back.dao;

import njnu.edu.back.pojo.Homework;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/07/27/11:15
 * @Description:
 */
@Repository
public interface HomeworkRepository extends MongoRepository<Homework, String> {
    Page<Homework> findAllByMemberId(String memberId, Pageable pageable);

    List<Homework> findAllByMemberIdAndState(String memberId, int state);
}
