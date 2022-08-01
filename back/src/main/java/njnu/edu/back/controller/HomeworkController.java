package njnu.edu.back.controller;

import com.alibaba.fastjson2.JSONObject;
import njnu.edu.back.common.result.JsonResult;
import njnu.edu.back.common.result.ResultUtils;
import njnu.edu.back.service.HomeworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/07/29/17:27
 * @Description:
 */
@RestController
@RequestMapping("/homework")
public class HomeworkController {
    @Autowired
    HomeworkService homeworkService;

    @RequestMapping(value = "/findAll/{page}/{size}", method = RequestMethod.GET)
    public JsonResult findAll(@PathVariable int page, @PathVariable int size) {
        return ResultUtils.success(homeworkService.findAll(page, size));
    }

    @RequestMapping(value = "/updateHomework/{id}/{state}", method = RequestMethod.PATCH)
    public JsonResult updateHomework(@PathVariable String id, @PathVariable int state) {
        homeworkService.updateHomework(id, state);
        return ResultUtils.success();
    }

    @RequestMapping(value = "/delHomework", method = RequestMethod.DELETE)
    public JsonResult delHomework(@RequestBody JSONObject jsonObject) {
        String id = jsonObject.getString("id");
        String memberId = jsonObject.getString("memberId");
        int page = jsonObject.getIntValue("page");
        int size = jsonObject.getIntValue("size");
        return ResultUtils.success(homeworkService.delHomework(id, memberId, page, size));
    }

    @CrossOrigin
    @RequestMapping(value = "/download/{fileName}", method = RequestMethod.GET)
    public void download(@PathVariable String fileName, HttpServletResponse response) {
        homeworkService.download(fileName, response);
    }

    @RequestMapping(value = "/fuzzyQuery", method = RequestMethod.POST)
    public JsonResult fuzzyQuery(@RequestBody JSONObject jsonObject) {
        String memberId = jsonObject.getString("memberId");
        int page = jsonObject.getIntValue("page");
        int size = jsonObject.getIntValue("size");
        return ResultUtils.success(homeworkService.fuzzyQuery(memberId, page, size));
    }

    @RequestMapping(value = "/compressHomework/{number}", method = RequestMethod.GET)
    public void compressHomework(@PathVariable int number, HttpServletResponse response) {
        homeworkService.compressHomework(number, response);

    }
}
