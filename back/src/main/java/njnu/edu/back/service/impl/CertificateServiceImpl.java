package njnu.edu.back.service.impl;

import njnu.edu.back.common.utils.CertUtil;
import njnu.edu.back.dao.CertificateRepository;
import njnu.edu.back.dao.UserRepository;
import njnu.edu.back.pojo.Certificate;
import njnu.edu.back.pojo.User;
import njnu.edu.back.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    UserRepository userRepository;

    @Value("${certAddress}")
    String certAddress;

    @Override
    public Map<String, Object> addCert(Certificate cert, int page, int size) {
        certificateRepository.save(cert);
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Certificate> certificatePage = certificateRepository.findAll(pageable);
        return CertUtil.page2map(certificatePage, certAddress);
    }

    @Override
    public Map<String, Object> fuzzyQuery(String type, String keyWord, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        if(type.equals("")) {
            if(keyWord.equals("")) {
                return CertUtil.page2map(certificateRepository.findAll(pageable), certAddress);
            } else {
                return CertUtil.page2map(certificateRepository.findAllByUserIdContains(keyWord, pageable), certAddress);
            }
        } else {
            if(keyWord.equals("")) {
                return CertUtil.page2map(certificateRepository.findAllByUserIdContains(keyWord, pageable), certAddress);
            } else {
                return CertUtil.page2map(certificateRepository.findAllByTypeAndUserIdContains(type, keyWord, pageable), certAddress);
            }
        }
    }

    @Override
    public void updateCert(Certificate cert) {
        certificateRepository.save(cert);
    }

    @Override
    public Map<String, Object> delCert(String id, String type, String keyWord, int page, int size) {
        certificateRepository.deleteById(id);
        return fuzzyQuery(type, keyWord, page, size);
    }

    @Override
    public Map<String, Object> importList(int count) {
        certificateRepository.deleteAll();
        List<User> users = userRepository.findAllByFinishedCountGreaterThanEqual(count);
        for(User user : users) {
            Certificate certificate = new Certificate(null, user.getId(), "train", user.getId() + "_1");
            certificateRepository.save(certificate);
        }
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(0, 20, sort);

        return CertUtil.page2map(certificateRepository.findAll(pageable), certAddress);
    }

    @Override
    public int checkFile(String fileNumber) {
        String path = certAddress + fileNumber + ".pptx";
        File file = new File(path);
        if(file.exists()) {
            return 1;
        }
        return 0;
    }
}
