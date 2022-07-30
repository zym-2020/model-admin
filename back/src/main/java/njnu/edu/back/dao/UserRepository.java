package njnu.edu.back.dao;

import njnu.edu.back.pojo.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/07/27/11:14
 * @Description:
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Page<User> findAllByNameContains(String name, Pageable pageable);
    Page<User> findAllByMemberIdContains(String memberId, Pageable pageable);
    Page<User> findAllByEmailContains(String email, Pageable pageable);
    Page<User> findAllByTeamIdContains(String teamId, Pageable pageable);

    User findByMemberId(String memberId);
    User findByEmail(String email);
    User findByTeamId(String teamId);
}
