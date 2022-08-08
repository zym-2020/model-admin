package njnu.edu.back.common.utils;

import njnu.edu.back.pojo.Certificate;
import org.springframework.data.domain.Page;

import java.io.File;
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
            String path = certAddress + certificate.getNumber() + ".pptx";
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
}
