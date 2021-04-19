package com.biu.wifi.campus.Tool;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 文件下载工具类
 *
 * @author zhangbin
 */
public class FileDownloadUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileDownloadUtil.class);

    /**
     * 每个线程下载的字节数
     */
    private long unitSize = 1000 * 1024;

    /**
     * 启动多个线程下载文件
     *
     * @param taskExecutor  任务调度器
     * @param httpClient    模拟httpClient客户端发送http请求，可以控制到请求文件的字节位置。
     * @param remoteFileUrl 要被下载的目标文件的远程服务器地址 http://{host}:{port}/{project}/xx.xml
     * @param localPath     保存到本地的本地路径 E://test//
     * @throws IOException
     */
    public void doDownload(ThreadPoolTaskExecutor taskExecutor, CloseableHttpClient httpClient, String remoteFileUrl,
                           String localPath) throws IOException {
        String fileName = new URL(remoteFileUrl).getFile();
        LOGGER.debug("远程文件名称：" + fileName);
        fileName = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.length()).replace("%20", " ");
        LOGGER.debug("本地文件名称：" + fileName);
        long fileSize = this.getRemoteFileSize(remoteFileUrl);

        String tempFile = localPath + System.currentTimeMillis() + fileName;
        this.createFile(tempFile, fileSize);

        Long threadCount = (fileSize / unitSize) + (fileSize % unitSize != 0 ? 1 : 0);
        long offset = 0;
        CountDownLatch end = new CountDownLatch(threadCount.intValue());
        if (fileSize < unitSize || fileSize == unitSize) {// 如果远程文件尺寸小于等于unitSize
            DownloadThread downloadThread = new DownloadThread(remoteFileUrl, localPath + fileName, offset, fileSize,
                    end, httpClient);
            taskExecutor.execute(downloadThread);
        } else {// 如果远程文件尺寸大于unitSize
            for (int i = 1; i < threadCount; i++) {
                DownloadThread downloadThread = new DownloadThread(remoteFileUrl, localPath + fileName, offset,
                        unitSize, end, httpClient);
                taskExecutor.execute(downloadThread);
                offset = offset + unitSize;
            }

            if (fileSize % unitSize != 0) {// 如果不能整除，则需要再创建一个线程下载剩余字节
                DownloadThread downloadThread = new DownloadThread(remoteFileUrl, localPath + fileName, offset,
                        fileSize - unitSize * (threadCount - 1), end, httpClient);
                taskExecutor.execute(downloadThread);
            }
        }

        try {
            end.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 删除随机文件
            FileUtils.forceDelete(new File(tempFile));
        }
    }

    /**
     * 获取远程文件尺寸
     */
    private long getRemoteFileSize(String remoteFileUrl) throws IOException {
        long fileSize = 0;
        HttpURLConnection httpConnection = (HttpURLConnection) new URL(remoteFileUrl).openConnection();
        httpConnection.setRequestMethod("HEAD");
        int responseCode = httpConnection.getResponseCode();

        if (responseCode >= 400) {
            LOGGER.debug("Web服务器响应错误!");
            return 0;
        }

        String sHeader;
        for (int i = 1; ; i++) {
            sHeader = httpConnection.getHeaderFieldKey(i);
            if (sHeader != null && sHeader.equals("Content-Length")) {
                LOGGER.debug("文件大小ContentLength:" + httpConnection.getContentLength());
                fileSize = Long.parseLong(httpConnection.getHeaderField(sHeader));
                break;
            }
        }

        return fileSize;
    }

    /**
     * 创建指定大小的文件
     */
    private void createFile(String fileName, long fileSize) throws IOException {
        File newFile = new File(fileName);
        RandomAccessFile raf = new RandomAccessFile(newFile, "rw");
        raf.setLength(fileSize);
        raf.close();
    }

    /**
     * 文件下载任务线程
     *
     * @author zhangbin
     */
    public class DownloadThread implements Runnable {
        /**
         * 待下载的文件
         */
        private String url = null;

        /**
         * 本地文件名
         */
        private String fileName = null;

        /**
         * 偏移量
         */
        private long offset = 0;

        /**
         * 分配给本线程的下载字节数
         */
        private long length = 0;

        private CountDownLatch end;

        private CloseableHttpClient httpClient;

        private HttpContext context;

        /**
         * @param url      下载文件地址
         * @param fileName 另存文件名
         * @param offset   本线程下载偏移量
         * @param length   本线程下载长度
         * @author zhangbin
         */
        public DownloadThread(String url, String file, long offset, long length, CountDownLatch end,
                              CloseableHttpClient httpClient) {
            this.url = url;
            this.fileName = file;
            this.offset = offset;
            this.length = length;
            this.end = end;
            this.httpClient = httpClient;
            this.context = new BasicHttpContext();
            LOGGER.debug("偏移量=" + offset + ";字节数=" + length);
        }

        public void run() {
            try {
                HttpGet httpGet = new HttpGet(this.url);
                httpGet.addHeader("Range", "bytes=" + this.offset + "-" + (this.offset + this.length - 1));
                CloseableHttpResponse response = httpClient.execute(httpGet, context);

                BufferedInputStream bis = new BufferedInputStream(response.getEntity().getContent());
                byte[] buff = new byte[1024];
                int bytesRead;
                File newFile = new File(fileName);
                RandomAccessFile raf = new RandomAccessFile(newFile, "rw");

                while ((bytesRead = bis.read(buff, 0, buff.length)) != -1) {
                    raf.seek(this.offset);
                    raf.write(buff, 0, bytesRead);
                    this.offset = this.offset + bytesRead;
                }
                raf.close();
                bis.close();
            } catch (ClientProtocolException e) {
                LOGGER.error("DownloadThread exception msg:{}", ExceptionUtils.getFullStackTrace(e));
            } catch (IOException e) {
                LOGGER.error("DownloadThread exception msg:{}", ExceptionUtils.getFullStackTrace(e));
            } finally {
                end.countDown();
                LOGGER.debug(end.getCount() + " is go on!");
            }
        }
    }
}
