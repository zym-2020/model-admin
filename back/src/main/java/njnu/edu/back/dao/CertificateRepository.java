package njnu.edu.back.dao;

import njnu.edu.back.pojo.Certificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/08/01/20:22
 * @Description:
 */
@Repository
public interface CertificateRepository extends MongoRepository<Certificate, String> {
    Page<Certificate> findAllByUserIdContains(String userId, Pageable pageable);

    Page<Certificate> findAllByTypeAndUserIdContains(String type, String useId, Pageable pageable);

    Page<Certificate> findAllByType(String type, Pageable pageable);
}
