package njnu.edu.back.controller;

import com.alibaba.fastjson2.JSONObject;
import njnu.edu.back.common.result.JsonResult;
import njnu.edu.back.common.result.ResultUtils;
import njnu.edu.back.pojo.Certificate;
import njnu.edu.back.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/08/01/21:04
 * @Description:
 */
@RestController
@RequestMapping("/cert")
public class CertificateController {
    @Autowired
    CertificateService certificateService;

    @RequestMapping(value = "/addCert/{page}/{size}", method = RequestMethod.POST)
    public JsonResult addCert(@RequestBody Certificate cert, @PathVariable int page, @PathVariable int size) {
        return ResultUtils.success(certificateService.addCert(cert, page, size));
    }

    @RequestMapping(value = "/fuzzyQuery", method = RequestMethod.POST)
    public JsonResult fuzzyQuery(@RequestBody JSONObject jsonObject) {
        String type = jsonObject.getString("type");
        String keyWord = jsonObject.getString("keyWord");
        int page = jsonObject.getIntValue("page");
        int size = jsonObject.getIntValue("size");
        return ResultUtils.success(certificateService.fuzzyQuery(type, keyWord, page, size));
    }

    @RequestMapping(value = "/updateCert", method = RequestMethod.PATCH)
    public JsonResult updateCert(@RequestBody Certificate cert) {
        certificateService.updateCert(cert);
        return ResultUtils.success();
    }

    @RequestMapping(value = "/delCert", method = RequestMethod.DELETE)
    public JsonResult delCert(@RequestBody JSONObject jsonObject) {
        String id = jsonObject.getString("id");
        String type = jsonObject.getString("type");
        String keyWord = jsonObject.getString("keyWord");
        int page = jsonObject.getIntValue("page");
        int size = jsonObject.getIntValue("size");
        return ResultUtils.success(certificateService.delCert(id, type, keyWord, page, size));
    }

    @RequestMapping(value = "/importList/{count}", method = RequestMethod.POST)
    public JsonResult importList(@PathVariable int count) {
        return ResultUtils.success(certificateService.importList(count));
    }

    @RequestMapping(value = "/checkFile/{fileNumber}", method = RequestMethod.GET)
    public JsonResult checkFile(@PathVariable String fileNumber) {
        return ResultUtils.success(certificateService.checkFile(fileNumber));
    }

    @RequestMapping(value = "/exportExcel/{type}", method = RequestMethod.GET)
    public void exportExcel(@PathVariable String type, HttpServletResponse response) {
        certificateService.exportExcel(type, response);
    }
}
