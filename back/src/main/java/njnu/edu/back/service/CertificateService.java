package njnu.edu.back.service;

import njnu.edu.back.pojo.Certificate;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/08/01/20:23
 * @Description:
 */
public interface CertificateService {
    Map<String, Object> addCert(Certificate cert, int page, int size);

    Map<String, Object> fuzzyQuery(String type, String keyWord, int page, int size);

    void updateCert(Certificate cert);

    Map<String, Object> delCert(String id, String type, String keyWord, int page, int size);

    Map<String, Object> importList(int count);

    int checkFile(String fileNumber);
}
