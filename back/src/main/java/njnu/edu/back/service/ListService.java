package njnu.edu.back.service;

import njnu.edu.back.pojo.User;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/08/05/14:37
 * @Description:
 */
public interface ListService {
    Page<User> getList(int number, String keyword, int page, int size);
}
