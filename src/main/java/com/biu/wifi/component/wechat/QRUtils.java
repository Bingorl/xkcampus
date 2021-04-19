package com.biu.wifi.component.wechat;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;

import com.swetake.util.Qrcode;


public class QRUtils {

    public static byte[] createQR(String msg) throws Exception {
        return QRUtils.createQR(msg, null);
    }

    /**
     * 创建二维码
     *
     * @param msg
     * @param logoImg
     * @return
     * @throws IOException
     */
    public static byte[] createQR(String msg, byte[] logoImg) throws Exception {

        byte[] qrImage = new byte[0];

        if (StringUtils.isNotBlank(msg)) {

            byte[] bstr = msg.getBytes("UTF-8");

            Graphics2D g = null;
            ByteArrayOutputStream out = null;

            try {

                BufferedImage bi = new BufferedImage(140, 140, BufferedImage.TYPE_INT_RGB);

                g = bi.createGraphics();

                // 背景颜色
                g.setBackground(new Color(238, 238, 236));
                g.clearRect(0, 0, 140, 140);

                // 条码颜色
                g.setColor(Color.BLACK);

                // 设置偏移量 不设置可能导致解析出错
                int pixoff = 2;

                if (bstr.length > 0 && bstr.length < 123) {

                    Qrcode qrcode = new Qrcode();
                    qrcode.setQrcodeErrorCorrect('M');
                    qrcode.setQrcodeEncodeMode('B');

                    // 设置二维码排错率，可选L(7%)、M(15%)、Q(25%)、H(30%)，排错率越高可存储的信息越少，但对二维码清晰度的要求越小
                    qrcode.setQrcodeVersion(7);

                    boolean[][] b = qrcode.calQrcode(bstr);
                    for (int i = 0; i < b.length; i++) {
                        for (int j = 0; j < b.length; j++) {
                            if (b[j][i]) {
                                g.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);
                            }
                        }
                    }
                }

                if (logoImg != null && logoImg.length > 0) {
                    g.drawImage(ImageIO.read(new ByteArrayInputStream(logoImg)), 48, 48, 45, 45, null);
                }

                bi.flush();

                out = new ByteArrayOutputStream();

                ImageIO.write(bi, "jpg", out);

                qrImage = out.toByteArray();
            } finally {

                if (g != null) {
                    g.dispose();
                }

                if (out != null) {
                    out.close();
                }

            }
        }

        return qrImage;
    }

    public static void main(String[] args) throws Exception {

        // ImageUtilsEx.resizePNG("D:/11111111111111111111/2012053113023532.png",
        // 72, 72, "D:/11111111111111111111/114.png", 100, 100);

//		FileUtils.writeByteArrayToFile(new File("D:/123456/QQ图片20150504163219.png"), QRUtils.createQR(
//				"http://www.iappk.com?我们的日志好日子的" + "22222222222222222222222222222222222222222222222222222222222222222222222", FileUtils
//						.readFileToByteArray(new File("D:/1111111111111111111111111111111/3b076e5f-af8a-4076-ab63-0473c8f4746e.PNG"))));
//		FileUtils.writeByteArrayToFile(new File("D:/123456/QQ图片20150504163219.png"),QRUtils.createQR("我们的日志好日子的"));

        // FileUtils.writeByteArrayToFile(new
        // File("D:/1111111111111111111111111111111/12333_2.png"),
        // ImageUtilsEx.resizeImage(
        // FileUtils.readFileToByteArray(new
        // File("D:/1111111111111111111111111111111/12333.png")), 300, 300,
        // "png"));
        //
        // FileUtils.writeByteArrayToFile(new
        // File("D:/1111111111111111111111111111111/2012053113023532_1.png"),
        // ImageUtilsEx.resizeImage(
        // FileUtils.readFileToByteArray(new
        // File("D:/1111111111111111111111111111111/2012053113023532.png")),
        // 300, 300, "png"));

        // ImageUtilsEx.compressPic("D:/11111111111111111111/2012053113023532.png",
        // "D:/11111111111111111111/111.png", 100, 100, false);

        // System.out.println(FileUtilsEx.getSuffix("21.png"));
    }
}
