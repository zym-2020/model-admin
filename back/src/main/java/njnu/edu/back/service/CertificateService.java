package njnu.edu.back.service;

import njnu.edu.back.pojo.Certificate;
import org.springframework.data.domain.Page;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/08/01/20:23
 * @Description:
 */
public interface CertificateService {
    Page<Certificate> addCert(Certificate cert, int page, int size);

    Page<Certificate> fuzzyQuery(String type, String keyWord, int page, int size);

    void updateCert(Certificate cert);

    Page<Certificate> delCert(String id, String type, String keyWord, int page, int size);
}
