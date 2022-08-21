package njnu.edu.back.common.utils;

import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;
import njnu.edu.back.pojo.Certificate;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/08/08/19:59
 * @Description:
 */
public class CertUtil {

    public static Map<String, Object> page2map(Page<Certificate> page, String certAddress) {
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        for(Certificate certificate : page.getContent()) {
            Map<String, Object> temp = new HashMap<>();
            temp.put("id", certificate.getId());
            temp.put("userId", certificate.getUserId());
            temp.put("number", certificate.getNumber());
            temp.put("type", certificate.getType());
            String path = certAddress + certificate.getNumber() + ".pdf";
            File file = new File(path);
            if(file.exists()) {
                temp.put("fileState", 1);
            } else {
                temp.put("fileState", 0);
            }
            list.add(temp);
        }
        map.put("list", list);
        map.put("total", page.getTotalElements());
        return map;
    }

    public static void createExcel(String path, List<Map<String, String>> list) {
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet();
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("编号");
        row.createCell(1).setCellValue("姓名");
        row.createCell(2).setCellValue("文件编号");
        for(int i = 0; i < list.size(); i++) {
            Row temp = sheet.createRow(i + 1);
            temp.createCell(0).setCellValue(list.get(i).get("id"));
            temp.createCell(1).setCellValue(list.get(i).get("name"));
            temp.createCell(2).setCellValue(list.get(i).get("fileId"));
        }
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(path);
            wb.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        } finally {
            try {
                if(outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
            }
        }
    }
}
