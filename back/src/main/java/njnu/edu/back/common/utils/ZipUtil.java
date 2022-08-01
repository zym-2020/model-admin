package njnu.edu.back.common.utils;

import net.lingala.zip4j.ZipFile;
import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/08/01/9:50
 * @Description:
 */
public class ZipUtil {
    public static void compressFile(String destination, List<String> fileList) {
        ZipFile zipFile = new ZipFile(destination);
        try {
            List<File> files = new ArrayList<>();
            for (String fileAddress : fileList) {
                files.add(new File(fileAddress));
            }
            zipFile.addFiles(files);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        }
    }
}
