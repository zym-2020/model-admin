package njnu.edu.back.dao;

import njnu.edu.back.pojo.Homework;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/07/27/11:15
 * @Description:
 */
@Repository
public interface HomeworkRepository extends MongoRepository<Homework, String> {
}
