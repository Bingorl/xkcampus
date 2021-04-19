package com.biu.wifi.campus.controller.app;

import com.biu.wifi.campus.Tool.FileDownloadUtil;
import com.biu.wifi.campus.service.JwCjcxService;
import com.biu.wifi.core.util.ServletUtilsEx;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class AppApkController {

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private JwCjcxService jwCjcxService;

    /**
     * 下载大文件
     *
     * @param request
     */
    @RequestMapping("/apk_download")
    public void fileDownload(HttpServletRequest request) {
        // 本地保存路径
        Object object = request.getParameter("storePath");
        String localPath = "/";
        if (object != null) {
            localPath = String.valueOf(object);
        }

        // 模拟httpClient客户端发送http请求
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(100);
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();

        System.out.println(ServletUtilsEx.getHostName(request));
        System.out.println(ServletUtilsEx.getHostURL(request));
        System.out.println(ServletUtilsEx.getHostURLWithContextPath(request));

        taskExecutor.setCorePoolSize(5);
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.setQueueCapacity(100);
        String remoteFileUrl = "https://app.54xy.com/campus/2018/10/09/20180101091938.mp4";

        try {
            new FileDownloadUtil().doDownload(taskExecutor, httpClient, remoteFileUrl, localPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
