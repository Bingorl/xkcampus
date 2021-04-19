package com.biu.wifi.component.datastore;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biu.wifi.component.datastore.fileio.FileIoEntity;
import com.biu.wifi.core.base.CoreService;
import com.biu.wifi.core.support.exception.ServiceException;
import com.biu.wifi.core.util.FileUtilsEx;
import com.biu.wifi.core.util.ImageUtilsEx;


@Service
public class ImageService extends CoreService {

    @Autowired
    private FileSupportService fileSupportService;

    /**
     * @return String
     * @description 图片的剪切
     */
    public String cut(String id, BigDecimal x, BigDecimal y, BigDecimal width,
                      BigDecimal height, BigDecimal scale) throws Exception {

        FileIoEntity fileInfo = fileSupportService.get(id);

        // validate param
        {
            x = (x == null) ? BigDecimal.ZERO : x;
            y = (y == null) ? BigDecimal.ZERO : y;
            x = (x.intValue() < 0) ? BigDecimal.ZERO : x;
            y = (y.intValue() < 0) ? BigDecimal.ZERO : y;

            String[] srcImg = fileInfo.getDataInfo().getDescription()
                    .split("\\|");

            if (x.intValue() > new Integer(srcImg[0])
                    || y.intValue() > new Integer(srcImg[1])) {
                throw new ServiceException("裁剪起始点必须在图片内,请重新裁剪!");
            }

            // 如果x+裁剪宽度>图片宽度,则修改裁剪宽度=图片宽度-x
            if (x.add(width).intValue() > new Integer(srcImg[0])) {
                width = new BigDecimal(srcImg[0]).subtract(x);
            }

            // 如果y+裁剪高度>图片高度,则修改裁剪高度=图片高度-y
            if (y.add(height).intValue() > new Integer(srcImg[1])) {
                height = new BigDecimal(srcImg[1]).subtract(y);
            }

            if (width == null || height == null || width.intValue() < 0
                    || height.intValue() < 0) {
                throw new ServiceException("裁剪尺寸必须>0,请重新裁剪!");
            }
        }

        try {
            byte[] result = ImageUtilsEx
                    .cut(fileInfo.getContent(), FileUtilsEx.getSuffix(fileInfo
                                    .getDataInfo().getFileName()),
                            x.divide(scale, 0, BigDecimal.ROUND_HALF_UP)
                                    .intValue(),
                            y.divide(scale, 0, BigDecimal.ROUND_HALF_UP)
                                    .intValue(),
                            width.divide(scale, 0, BigDecimal.ROUND_HALF_UP)
                                    .intValue(),
                            height.divide(scale, 0, BigDecimal.ROUND_HALF_UP)
                                    .intValue());

            return fileSupportService.add(fileInfo.getDataInfo().getFileName(),
                    result, "ds_upload");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            logger.error("##################################id:" + id + "x:"
                    + x + "y:" + y + "width:" + width + "height:" + height
                    + "scale:" + scale);
            throw new ServiceException("图片格式不符,请把此图片用画图工具另存后重新上传,再进行裁剪!");
        }
    }
}
