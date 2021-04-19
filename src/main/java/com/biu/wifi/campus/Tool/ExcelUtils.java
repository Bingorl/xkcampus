package com.biu.wifi.campus.Tool;

import com.biu.wifi.core.support.exception.ServiceException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

public class ExcelUtils {

    private static Log logger = LogFactory.getLog(ExcelUtils.class);
    private final static String Excel_2003 = ".xls"; // 2003 版本的excel
    private final static String Excel_2007 = ".xlsx"; // 2007 版本的exce

    private Workbook rwb;

    private ExcelUtils(Workbook wb) {
        this.rwb = wb;
    }

    public static ExcelUtils getInstance(byte[] bytes, String fileName) throws Exception {
        Workbook work = null;
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if (Excel_2003.equals(fileType.toLowerCase())) {
            work = new HSSFWorkbook((InputStream) (new ByteArrayInputStream(bytes)));// 2003 版本的excel
        } else if (Excel_2007.equals(fileType.toLowerCase())) {
            work = new XSSFWorkbook((InputStream) (new ByteArrayInputStream(bytes)));// 2007 版本的excel
        } else {
            throw new Exception("解析文件格式有误！");
        }
        return new ExcelUtils(work);
    }

    public Integer getSheetRow(int sheet) {
        return Integer.valueOf(this.rwb.getSheetAt(sheet).getPhysicalNumberOfRows());
    }

    public String read(int sheet, int column, int row) {
        try {
            return this.rwb.getSheetAt(sheet).getRow(row).getCell(column) + "";
        } catch (Exception arg4) {
            logger.error("excel读取失败:", arg4);
            throw new ServiceException("excel读取失败:" + arg4.getMessage());
        }
    }

    public String readByCheck(int sheet, int column, int row) {
        try {
            // 根据表格的单元格的数据类型进行处理
            Cell cell = this.rwb.getSheetAt(sheet).getRow(row).getCell(column);
            if (cell == null) {
                return "";
            }
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_BLANK:
                    return "";
                case Cell.CELL_TYPE_BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                case Cell.CELL_TYPE_ERROR:
                    return String.valueOf(cell.getErrorCellValue());
                case Cell.CELL_TYPE_FORMULA:
                    return cell.getCellFormula();
                case Cell.CELL_TYPE_NUMERIC:
                    DecimalFormat df = new DecimalFormat();
                    String value = df.format(cell.getNumericCellValue());
                    value = value.replaceAll(",", "");
                    return value;
                case Cell.CELL_TYPE_STRING:
                    return cell.getStringCellValue();
                default:
                    break;
            }

            return "";
        } catch (Exception arg4) {
            logger.error("excel读取失败:", arg4);
            throw new ServiceException("excel读取失败:" + arg4.getMessage());
        }
    }

    public Integer getSheetCout() {
        return this.rwb.getNumberOfSheets();
    }

    /**
     * 导出记录的excel数据(包含多个sheet)
     *
     * @param dataMap 要导出的数据源
     * @param xlsName 文件名
     * @return
     */
    public static String write(Map<String, List<List<String>>> dataMap, String xlsName, String storeDir) {
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet
        for (String sheetName : dataMap.keySet()) {
            //sheetName为工作表的sheet名称
            HSSFSheet sheet = wb.createSheet(sheetName);
            // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
            HSSFRow row = sheet.createRow(0);
            // 第四步，创建单元格，并设置值表头 设置表头居中
            HSSFCellStyle style = wb.createCellStyle();
            // 创建一个居左格式
            style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            // 第五步，写入实体数据 实际应用中这些数据从数据库具体的table得到，
            List<List<String>> objList = dataMap.get(sheetName);
            for (int i = 0; i < objList.size(); i++) {
                row = sheet.createRow(i);
                for (int j = 0; j < objList.get(i).size(); j++) {
                    row.createCell(j).setCellValue(objList.get(i).get(j));
                }
            }
        }

        return writeExcelFile(xlsName, wb, storeDir);
    }

    /**
     * 写出excel文件
     *
     * @param xlsName
     * @param wb
     * @param storeDir
     * @return
     */
    public static String writeExcelFile(String xlsName, HSSFWorkbook wb, String storeDir) {
        try {
            File dir = new File(storeDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            xlsName += ".xls";
            File excelFile = new File(storeDir, xlsName);
            FileOutputStream fos = new FileOutputStream(excelFile);
            wb.write(fos);
            fos.close();
            //全路径
            if (!storeDir.endsWith(File.separator))
                xlsName = storeDir + File.separator + xlsName;
            return xlsName;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("导出excel文件出错：" + e.getMessage());
            return null;
        }
    }
}
