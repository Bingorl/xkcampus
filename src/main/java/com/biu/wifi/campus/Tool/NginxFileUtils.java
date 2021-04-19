package com.biu.wifi.campus.Tool;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biu.wifi.component.datastore.DataStoreService;
import com.biu.wifi.component.datastore.fileio.FileIoEntity;
import com.biu.wifi.core.support.SpringContextLoader;
import com.biu.wifi.core.support.exception.ServiceException;
import com.biu.wifi.core.support.session.SessionContext;
import com.biu.wifi.core.util.DateUtilsEx;
import com.biu.wifi.core.util.FileUtilsEx;
import com.biu.wifi.core.util.ImageUtilsEx;
import com.biu.wifi.core.util.StringUtilsEx;
import com.biu.wifi.dao.TCptDataInfoMapper;
import com.biu.wifi.model.TCptDataInfo;

public class NginxFileUtils {

    private static Log logger = LogFactory.getLog(NginxFileUtils.class);

    public static String add(String fileName, byte[] fileContent, String storeName, String fileImgSize) {

        TCptDataInfo dataInfo = new TCptDataInfo();

        // 文件信息
        {
            dataInfo.setCreateDate(DateUtilsEx.getSysDate());

            dataInfo.setId(DateUtilsEx.formatToString(dataInfo.getCreateDate(), DateUtilsEx.DATE_FORMAT_DAY).replace(".", File.separator) + File.separator + StringUtilsEx.getUUID() + "." + FileUtilsEx.getSuffix(fileName));

            dataInfo.setCreateUser(SessionContext.getSessionInfo() != null ? SessionContext.getSessionInfo().getUserId() : null);

            dataInfo.setFileName(fileName);

            dataInfo.setFileSize(BigDecimal.valueOf(fileContent.length));

            dataInfo.setStatus(BigDecimal.valueOf(0));

            dataInfo.setStoreName(storeName);
        }

        // 如果设置图片尺寸,并且是图片类型的文件则进行压缩
        if (StringUtils.isNotBlank(fileImgSize) && FileUtilsEx.isImage(fileName)) {

            fileImgSize = fileImgSize.replace(",", "|").replace("x", "|");

            String[] sizeArray = fileImgSize.split("\\|");

            try {
                fileContent = ImageUtilsEx.resizeImage(fileContent, Integer.valueOf(sizeArray[0]), Integer.valueOf(sizeArray[1]), FileUtilsEx.getSuffix(fileName));

                // 重新尺寸
                dataInfo.setFileSize(BigDecimal.valueOf(fileContent.length));
            } catch (Exception e) {
                logger.error("图片压缩出错:" + e.getMessage(), e);
            }
        }

        // 文件实际保存
        {
            FileIoEntity entity = new FileIoEntity();
            entity.setDataInfo(dataInfo);
            entity.setContent(fileContent);
            save(entity);
        }

        // 图片的话，设置具体尺寸
        setDescription(dataInfo, fileContent);

        // 文件信息保存
        TCptDataInfoMapper dataInfoDao = (TCptDataInfoMapper) SpringContextLoader.getBean("tCptDataInfoMapper");
        dataInfoDao.insert(dataInfo);

        return dataInfo.getId();
    }

    private static void setDescription(TCptDataInfo dataInfo, byte[] fileContent) {

        if (!FileUtilsEx.isImage(dataInfo.getFileName()))
            return;
        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(fileContent));
            image.getHeight();
            dataInfo.setDescription(String.valueOf(image.getWidth()) + "|" + String.valueOf(image.getHeight()));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static void save(FileIoEntity entity) {
        saveFile(entity.getDataInfo().getId(), entity.getContent(), getPath(entity.getDataInfo().getStoreName()));
    }

    private static void saveFile(String id, byte[] content, String storePath) {

        File file = new File(storePath + id);

        if (file.exists()) {
            throw new ServiceException("文件:" + file.getPath() + "已经存在!");
        }

        // 文件夹不存在的话则创建
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        try {
            FileUtils.writeByteArrayToFile(file, content);
        } catch (IOException e) {
            throw new ServiceException(e);
        }
    }

    private static String getPath(String storeName) {
        DataStoreService dataStoreService = (DataStoreService) SpringContextLoader.getBean("dataStoreService");
        String storePath = dataStoreService.getInfoByName(storeName).getPath();
        return storePath;
    }

    //二进制流转文件
    public static File byte2File(byte[] buf, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(buf);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    public static File get(String id) {

        TCptDataInfoMapper dataInfoDao = (TCptDataInfoMapper) SpringContextLoader.getBean("tCptDataInfoMapper");

        TCptDataInfo dataInfo = dataInfoDao.selectByPrimaryKey(id);

        String path = getPath(dataInfo.getStoreName());

        return new File(path + id);
    }

}
