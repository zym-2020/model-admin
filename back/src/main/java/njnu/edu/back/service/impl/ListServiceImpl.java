package njnu.edu.back.service.impl;

import njnu.edu.back.dao.HomeworkRepository;
import njnu.edu.back.dao.UserRepository;
import njnu.edu.back.pojo.User;
import njnu.edu.back.service.ListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/08/05/14:38
 * @Description:
 */
@Service
public class ListServiceImpl implements ListService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    HomeworkRepository homeworkRepository;


    @Override
    public Page<User> getList(int number, String keyword, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        if(keyword.equals("")) {
            return userRepository.findAllByFinishedCountGreaterThanEqual(number, pageable);
        } else {
            return userRepository.findAllByMemberIdAndFinishedCountGreaterThanEqual(keyword, number, pageable);
        }

    }
}
