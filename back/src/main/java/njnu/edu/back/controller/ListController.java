package njnu.edu.back.controller;

import com.alibaba.fastjson2.JSONObject;
import njnu.edu.back.common.result.JsonResult;
import njnu.edu.back.common.result.ResultUtils;
import njnu.edu.back.service.ListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/08/08/14:21
 * @Description:
 */
@RestController
@RequestMapping("/list")
public class ListController {
    @Autowired
    ListService listService;

    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    public JsonResult getList(@RequestBody JSONObject jsonObject) {
        int number = jsonObject.getIntValue("number");
        String keyword = jsonObject.getString("keyword");
        int page = jsonObject.getIntValue("page");
        int size = jsonObject.getIntValue("size");
        return ResultUtils.success(listService.getList(number, keyword, page, size));
    }
}
