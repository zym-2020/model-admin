package njnu.edu.back.service;

import njnu.edu.back.pojo.Homework;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/07/29/17:17
 * @Description:
 */
public interface HomeworkService {
    Page<Homework> findAll(int page, int size);

    void updateHomework(String id, int state);

    Page<Homework> delHomework(String id, String memberId, int page, int size);

    void download(String fileName, HttpServletResponse response);
}
