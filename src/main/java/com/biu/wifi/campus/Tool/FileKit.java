package com.biu.wifi.campus.Tool;

import com.biu.wifi.campus.exception.BizException;
import com.biu.wifi.campus.result.Result;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author zhangbin.
 * @date 2019/3/22.
 */
public class FileKit {

    public static String now = new SimpleDateFormat("yyyy/MM/dd").format(new Date());

    public static void main(String[] args) throws IOException {
        write("https://cdn.natapp.cn/uploads/ueditor/php/upload/image/20170118/1484723077222282.png",
                "C:\\Users\\zhangbin\\Desktop\\");
    }

    /**
     * 写文件
     *
     * @param source   图片的网络地址
     * @param savePath 文件在服务器的保存目录
     * @throws IOException
     */
    public static String write(String source, String savePath) throws IOException {
        URL url = new URL(source);
        URLConnection urlConnection = url.openConnection();
        InputStream in = urlConnection.getInputStream();
        String contentType = urlConnection.getContentType();

        String fileName = UUID.randomUUID().toString() + getSubFix(contentType);

        File dir = mkdirs(savePath);
        File file = new File(dir.getPath(), fileName);
        FileOutputStream fos = new FileOutputStream(file);
        writeIO(in, fos);
        return fileName;
    }

    public static void writeIO(InputStream in, FileOutputStream fos) throws IOException {
        int len;
        byte[] buf = new byte[1024];
        while ((len = in.read(buf)) != -1) {
            fos.write(buf, 0, len);
        }
        fos.close();
        in.close();
    }

    /**
     * 创建文件保存目录(根目录+日期子目录)
     *
     * @param rootPath 服务器保存目录的根目录
     */
    public static File mkdirs(String rootPath) {
        String path = new StringBuffer(rootPath)
                .append(now)
                .toString();
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 获取图片的后缀
     *
     * @param contentType
     * @return
     */
    private static String getSubFix(String contentType) {
        if (StringUtils.isEmpty(contentType)) {
            throw new BizException(Result.CUSTOM_MESSAGE, "未知文件错误");
        }
        if (MediaType.IMAGE_JPEG_VALUE.equalsIgnoreCase(contentType)) {
            return ".jpg";
        } else if (MediaType.IMAGE_PNG_VALUE.equalsIgnoreCase(contentType)) {
            return ".png";
        } else if (MediaType.IMAGE_GIF_VALUE.equalsIgnoreCase(contentType)) {
            return ".gif";
        } else {
            throw new BizException(Result.CUSTOM_MESSAGE, "图片格式只支持jpg,png和gif");
        }
    }
}
