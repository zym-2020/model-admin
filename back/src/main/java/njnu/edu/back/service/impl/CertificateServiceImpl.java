package njnu.edu.back.service.impl;

import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;
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

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.*;

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

    @Value("${tempAddress}")
    String tempAddress;

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
        String path = certAddress + fileNumber + ".pdf";
        File file = new File(path);
        if(file.exists()) {
            return 1;
        }
        return 0;
    }

    @Override
    public void exportExcel(String type, HttpServletResponse response) {
        List<Certificate> cerList = certificateRepository.findAll();
        List<Map<String, String>> resultList = new ArrayList<>();
        for(Certificate certificate : cerList) {
            if(certificate.getType().equals(type)) {
                Map<String, String> map = new HashMap<>();
                Optional<User> userOptional = userRepository.findById(certificate.getUserId());
                if(userOptional.isPresent()) {
                    User user = userOptional.get();
                    if(type.equals("train")) {
                        map.put("id", user.getMemberId());
                    } else {
                        map.put("id", user.getTeamId());
                    }
                    map.put("name", user.getName());
                    map.put("fileId", certificate.getNumber());
                    resultList.add(map);
                }
            }
        }
        String path = tempAddress + "list.xls";
        CertUtil.createExcel(path, resultList);
        InputStream in = null;
        ServletOutputStream sos = null;
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("list.xls", "UTF-8"));
            in = new FileInputStream(path);
            sos = response.getOutputStream();
            byte[] bytes = new byte[1024];
            while((in.read(bytes)) > -1) {
                sos.write(bytes);
            }
            sos.flush();
            sos.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        } finally {
            try {
                if(in != null) {
                    in.close();
                }
                if(sos != null) {
                    sos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
            }
        }
    }
}
