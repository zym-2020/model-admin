package njnu.edu.back.service.impl;

import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;
import njnu.edu.back.dao.HomeworkRepository;
import njnu.edu.back.pojo.Homework;
import njnu.edu.back.service.HomeworkService;
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
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/07/29/17:18
 * @Description:
 */
@Service
public class HomeworkServiceImpl implements HomeworkService {
    @Autowired
    HomeworkRepository homeworkRepository;

    @Value("${homeworkAddress}")
    String homeworkAddress;

    @Override
    public Page<Homework> findAll(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        return homeworkRepository.findAll(pageable);
    }

    @Override
    public void updateHomework(String id, int state) {
        Optional<Homework> optionalHomework = homeworkRepository.findById(id);
        if(!optionalHomework.isPresent()) {
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        }
        Homework homework = optionalHomework.get();
        homework.setState(state);
        homeworkRepository.save(homework);
    }

    @Override
    public Page<Homework> delHomework(String id, String memberId, int page, int size) {
        homeworkRepository.deleteById(id);
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        if(memberId.equals("")) {
            return homeworkRepository.findAll(pageable);
        }

        return homeworkRepository.findAllByMemberId(memberId, pageable);
    }

    @Override
    public void download(String fileName, HttpServletResponse response) {
        String path = homeworkAddress + fileName;
        File file = new File(path);
        if(!file.exists()) {
            throw new MyException(ResultEnum.NO_OBJECT);
        }
        InputStream in = null;
        ServletOutputStream sos = null;
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.addHeader("Content-Length", "" + file.length());
            in = new FileInputStream(file);
            sos = response.getOutputStream();
            byte[] bytes = new byte[1024];
            while((in.read(bytes)) > -1) {
                sos.write(bytes);
            }
            sos.flush();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
            }
        }
    }
}
