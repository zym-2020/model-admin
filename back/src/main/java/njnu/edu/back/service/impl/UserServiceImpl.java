package njnu.edu.back.service.impl;

import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;
import njnu.edu.back.dao.UserRepository;
import njnu.edu.back.pojo.User;
import njnu.edu.back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/07/27/16:41
 * @Description:
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public Page<User> fuzzyQuery(String type, String keyWord, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        if(keyWord.equals("")) {
            Page<User> users = userRepository.findAll(pageable);
            return users;
        }
        switch (type) {
            case "name":
                return userRepository.findAllByNameContains(keyWord, pageable);
            case "email":
                return userRepository.findAllByEmailContains(keyWord, pageable);
            case "memberId":
                return userRepository.findAllByMemberIdContains(keyWord, pageable);
            default:
                return userRepository.findAllByTeamIdContains(keyWord, pageable);
        }
    }

    @Override
    public void updateUserInfo(String id, User user) {
        user.setId(id);
        userRepository.save(user);
    }
}
