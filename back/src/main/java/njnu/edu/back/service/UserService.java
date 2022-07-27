package njnu.edu.back.service;

import njnu.edu.back.pojo.User;
import org.springframework.data.domain.Page;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/07/27/16:41
 * @Description:
 */
public interface UserService {
    Page<User> fuzzyQuery(String type, String keyWord, int page, int size);

    void updateUserInfo(String id, User user);
}
