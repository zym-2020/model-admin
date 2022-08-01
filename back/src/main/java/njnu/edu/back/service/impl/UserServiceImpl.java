package njnu.edu.back.service.impl;

import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;
import njnu.edu.back.dao.UserRepository;
import njnu.edu.back.pojo.User;
import njnu.edu.back.service.UserService;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Iterator;
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
        String email = user.getEmail();
        String memberId = user.getMemberId();
        User user1 = userRepository.findByEmail(email);
        User user2 = userRepository.findByMemberId(memberId);
        if(!email.equals("") && user1 != null && !user1.getId().equals(id)) {
            throw new MyException(-99, "邮箱已经存在!");
        }
        if(!memberId.equals("") && user2 != null && !user2.getId().equals(id)) {
            throw new MyException(-99, "学员编号已经存在!");
        }

        user.setId(id);
        userRepository.save(user);
    }

    @Override
    public Page<User> addUser(User user, int page, int size) {
        String email = user.getEmail();
        String memberId = user.getMemberId();
        if(!email.equals("") && (userRepository.findByEmail(email)) != null) {
            throw new MyException(-99, "邮箱已经存在!");
        }
        if(!memberId.equals("") && (userRepository.findByMemberId(memberId)) != null) {
            throw new MyException(-99, "学员编号已经存在!");
        }

        userRepository.save(user);
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<User> delUser(String id, String type, String keyWord, int page, int size) {
        userRepository.deleteById(id);
        return fuzzyQuery(type, keyWord, page, size);
    }

    /**
    * @Description:批量插入用户数据，但spring data批量插入还不会用，后期有时间优化
    * @Author: Yiming
    * @Date: 2022/7/28
    */

    @Override
    public void batchInsert(MultipartFile file, int sheetNumber) {
        try {
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(file.getInputStream());
            XSSFSheet sheet = xssfWorkbook.getSheetAt(sheetNumber);
            System.out.println(sheet.getLastRowNum());
            for(int i = 1703; i <= sheet.getLastRowNum(); i++) {
                XSSFRow row = sheet.getRow(i);
                System.out.println(i);
                User user = new User(null, "", row.getCell(2).getStringCellValue(), row.getCell(7).getStringCellValue(), NumberToTextConverter.toText(row.getCell(1).getNumericCellValue()));
                userRepository.save(user);
//                System.out.println(NumberToTextConverter.toText(row.getCell(2).getNumericCellValue()) + row.getCell(1).getStringCellValue() + row.getCell(8).getStringCellValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        }
    }

    @Override
    public String checkUserId(String userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isPresent()) {
            return optionalUser.get().getName();
        } else {
            throw new MyException(ResultEnum.NO_OBJECT);
        }
    }
}
