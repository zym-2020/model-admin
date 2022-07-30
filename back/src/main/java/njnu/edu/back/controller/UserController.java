package njnu.edu.back.controller;

import com.alibaba.fastjson2.JSONObject;
import njnu.edu.back.common.result.JsonResult;
import njnu.edu.back.common.result.ResultUtils;
import njnu.edu.back.pojo.User;
import njnu.edu.back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/07/27/16:41
 * @Description:
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/fuzzyQuery", method = RequestMethod.POST)
    public JsonResult fuzzyQuery(@RequestBody JSONObject jsonObject) {
        String type = jsonObject.getString("type");
        String keyWord = jsonObject.getString("keyWord");
        int page = jsonObject.getIntValue("page");
        int size = jsonObject.getIntValue("size");
        return ResultUtils.success(userService.fuzzyQuery(type, keyWord, page, size));
    }

    @RequestMapping(value = "/updateUserInfo/{id}", method = RequestMethod.PATCH)
    public JsonResult updateUserInfo(@PathVariable String id, @RequestBody User user) {
        userService.updateUserInfo(id, user);
        return ResultUtils.success();
    }

    @RequestMapping(value = "/addUserInfo/{page}/{size}", method = RequestMethod.POST)
    public JsonResult addUserInfo(@RequestBody User user, @PathVariable int page, @PathVariable int size) {
        return ResultUtils.success(userService.addUser(user, page, size));
    }

    @RequestMapping(value = "/delUser", method = RequestMethod.DELETE)
    public JsonResult delUser(@RequestBody JSONObject jsonObject) {
        String id = jsonObject.getString("id");
        String type = jsonObject.getString("type");
        String keyWord = jsonObject.getString("keyWord");
        int page = jsonObject.getIntValue("page");
        int size = jsonObject.getIntValue("size");
        return ResultUtils.success(userService.delUser(id, type, keyWord, page, size));
    }

    @RequestMapping(value = "/batchInsert", method = RequestMethod.POST)
    public JsonResult batchInsert(@RequestParam MultipartFile file, @RequestParam int sheetNumber) {
        userService.batchInsert(file,sheetNumber);
        return ResultUtils.success();
    }
}
