package njnu.edu.back.controller;

import com.alibaba.fastjson2.JSONObject;
import njnu.edu.back.common.result.JsonResult;
import njnu.edu.back.common.result.ResultUtils;
import njnu.edu.back.pojo.User;
import njnu.edu.back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
