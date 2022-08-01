package njnu.edu.back.service;

import njnu.edu.back.pojo.User;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

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

    Page<User> addUser(User user, int page, int size);

    Page<User> delUser(String id, String type, String keyWord, int page, int size);

    void batchInsert(MultipartFile file, int sheetNumber);

    String checkUserId(String userId);
}
