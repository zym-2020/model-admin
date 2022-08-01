package njnu.edu.back.service.impl;

import njnu.edu.back.dao.CertificateRepository;
import njnu.edu.back.pojo.Certificate;
import njnu.edu.back.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/08/01/20:23
 * @Description:
 */
@Service
public class CertificateServiceImpl implements CertificateService {
    @Autowired
    CertificateRepository certificateRepository;

    @Override
    public Page<Certificate> addCert(Certificate cert, int page, int size) {
        certificateRepository.save(cert);
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);

        return certificateRepository.findAll(pageable);
    }

    @Override
    public Page<Certificate> fuzzyQuery(String type, String keyWord, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        if(type.equals("")) {
            if(keyWord.equals("")) {
                return certificateRepository.findAll(pageable);
            } else {
                return certificateRepository.findAllByUserIdContains(keyWord, pageable);
            }
        } else {
            if(keyWord.equals("")) {
                return certificateRepository.findAllByType(type, pageable);
            } else {
                return certificateRepository.findAllByTypeAndUserIdContains(type, keyWord, pageable);
            }
        }
    }

    @Override
    public void updateCert(Certificate cert) {
        certificateRepository.save(cert);
    }

    @Override
    public Page<Certificate> delCert(String id, String type, String keyWord, int page, int size) {
        certificateRepository.deleteById(id);
        return fuzzyQuery(type, keyWord, page, size);
    }
}
