package com.wison.purchase.packages.comm.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.IdUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import com.wison.purchase.packages.comm.convert.ExcelBigNumberConvert;
import com.wison.purchase.packages.comm.convert.ExcelDateConvert;
import com.wison.purchase.packages.comm.domain.ExcelListener;
import com.wison.purchase.packages.comm.domain.ExcelResult;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Excel相关处理
 *
 * @author Lion Li
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExcelUtil {

    /**
     * 同步导入(适用于小数据量)
     *
     * @param is 输入流
     * @return 转换后集合
     */
    public static <T> List<T> importExcel(InputStream is, Class<T> clazz) {
        return EasyExcel.read(is).head(clazz).autoCloseStream(false).sheet().doReadSync();
    }


    /**
     * 使用校验监听器 异步导入 同步返回
     *
     * @param is         输入流
     * @param clazz      对象类型
     * @param isValidate 是否 Validator 检验 默认为是
     * @return 转换后集合
     */
    public static <T> ExcelResult<T> importExcel(InputStream is, Class<T> clazz, boolean isValidate) {
        DefaultExcelListener<T> listener = new DefaultExcelListener<>(isValidate);
        EasyExcel.read(is, clazz, listener).sheet().doRead();
        return listener.getExcelResult();
    }

    /**
     * 使用自定义监听器 异步导入 自定义返回
     *
     * @param is       输入流
     * @param clazz    对象类型
     * @param listener 自定义监听器
     * @return 转换后集合
     */
    public static <T> ExcelResult<T> importExcel(InputStream is, Class<T> clazz, ExcelListener<T> listener) {
        EasyExcel.read(is, clazz, listener).sheet().doRead();
        return listener.getExcelResult();
    }

    /**
     * 单表多数据模板导出 模板格式为 {.属性}
     *
     * @param filename     文件名
     * @param templatePath 模板路径 resource 目录下的路径包括模板文件名
     *                     例如: excel/temp.xlsx
     *                     重点: 模板文件必须放置到启动类对应的 resource 目录下
     * @param data         模板需要的数据
     * @param response     响应体
     */
    public static void exportTemplate(List<Object> data, String filename, String templatePath, HttpServletResponse response) {
        try {
            resetResponse(filename, response);
            ServletOutputStream os = response.getOutputStream();
            exportTemplate(data, templatePath, os);
        } catch (IOException e) {
            throw new RuntimeException("导出Excel异常");
        }
    }

    /**
     * 单表多数据模板导出 模板格式为 {.属性}
     *
     * @param templatePath 模板路径 resource 目录下的路径包括模板文件名
     *                     例如: excel/temp.xlsx
     *                     重点: 模板文件必须放置到启动类对应的 resource 目录下
     * @param data         模板需要的数据
     * @param os           输出流
     */
    public static void exportTemplate(List<Object> data, String templatePath, OutputStream os) {
        ClassPathResource templateResource = new ClassPathResource(templatePath);
        ExcelWriter excelWriter = EasyExcel.write(os)
                .withTemplate(templateResource.getStream())
                .autoCloseStream(false)
                // 大数值自动转换 防止失真
                .registerConverter(new ExcelBigNumberConvert())
                .build();
        WriteSheet writeSheet = EasyExcel.writerSheet().build();
        if (CollUtil.isEmpty(data)) {
            throw new IllegalArgumentException("数据为空");
        }
        // 单表多数据导出 模板格式为 {.属性}
        for (Object d : data) {
            excelWriter.fill(d, writeSheet);
        }
        excelWriter.finish();
    }

    /**
     * 多表多数据模板导出 模板格式为 {key.属性}
     *
     * @param filename     文件名
     * @param templatePath 模板路径 resource 目录下的路径包括模板文件名
     *                     例如: excel/temp.xlsx
     *                     重点: 模板文件必须放置到启动类对应的 resource 目录下
     * @param data         模板需要的数据
     * @param response     响应体
     */
    public static void exportTemplateMultiList(Map<String, Object> data, String filename, String templatePath, HttpServletResponse response) {
        try {
            resetResponse(filename, response);
            ServletOutputStream os = response.getOutputStream();
            exportTemplateMultiList(data, templatePath, os);
        } catch (IOException e) {
            throw new RuntimeException("导出Excel异常");
        }
    }

    /**
     * 多sheet模板导出 模板格式为 {key.属性}
     *
     * @param filename     文件名
     * @param templatePath 模板路径 resource 目录下的路径包括模板文件名
     *                     例如: excel/temp.xlsx
     *                     重点: 模板文件必须放置到启动类对应的 resource 目录下
     * @param data         模板需要的数据
     * @param response     响应体
     */
    public static void exportTemplateMultiSheet(List<Map<String, Object>> data, String filename, String templatePath, HttpServletResponse response) {
        try {
            resetResponse(filename, response);
            ServletOutputStream os = response.getOutputStream();
            exportTemplateMultiSheet(data, templatePath, os);
        } catch (IOException e) {
            throw new RuntimeException("导出Excel异常");
        }
    }

    /**
     * 多表多数据模板导出 模板格式为 {key.属性}
     *
     * @param templatePath 模板路径 resource 目录下的路径包括模板文件名
     *                     例如: excel/temp.xlsx
     *                     重点: 模板文件必须放置到启动类对应的 resource 目录下
     * @param data         模板需要的数据
     * @param os           输出流
     */
    public static void exportTemplateMultiList(Map<String, Object> data, String templatePath, OutputStream os) {
        ClassPathResource templateResource = new ClassPathResource(templatePath);
        ExcelWriter excelWriter = EasyExcel.write(os)
                .withTemplate(templateResource.getStream())
                .autoCloseStream(false)
                // 大数值自动转换 防止失真
                .registerConverter(new ExcelBigNumberConvert())
                .build();
        WriteSheet writeSheet = EasyExcel.writerSheet().build();
        if (CollUtil.isEmpty(data)) {
            throw new IllegalArgumentException("数据为空");
        }
        for (Map.Entry<String, Object> map : data.entrySet()) {
            // 设置列表后续还有数据
            FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
            if (map.getValue() instanceof Collection) {
                // 多表导出必须使用 FillWrapper
                excelWriter.fill(new FillWrapper(map.getKey(), (Collection<?>) map.getValue()), fillConfig, writeSheet);
            } else {
                excelWriter.fill(map.getValue(), writeSheet);
            }
        }
        excelWriter.finish();
    }

    /**
     * 多sheet模板导出 模板格式为 {key.属性}
     *
     * @param templatePath 模板路径 resource 目录下的路径包括模板文件名
     *                     例如: excel/temp.xlsx
     *                     重点: 模板文件必须放置到启动类对应的 resource 目录下
     * @param data         模板需要的数据
     * @param os           输出流
     */
    public static void exportTemplateMultiSheet(List<Map<String, Object>> data, String templatePath, OutputStream os) {
        ClassPathResource templateResource = new ClassPathResource(templatePath);
        ExcelWriter excelWriter = EasyExcel.write(os)
                .withTemplate(templateResource.getStream())
                .autoCloseStream(false)
                // 大数值自动转换 防止失真
                .registerConverter(new ExcelBigNumberConvert())
                .build();
        if (CollUtil.isEmpty(data)) {
            throw new IllegalArgumentException("数据为空");
        }
        for (int i = 0; i < data.size(); i++) {
            WriteSheet writeSheet = EasyExcel.writerSheet(i).build();
            for (Map.Entry<String, Object> map : data.get(i).entrySet()) {
                // 设置列表后续还有数据
                FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
                if (map.getValue() instanceof Collection) {
                    // 多表导出必须使用 FillWrapper
                    excelWriter.fill(new FillWrapper(map.getKey(), (Collection<?>) map.getValue()), fillConfig, writeSheet);
                } else {
                    Map<String, Object> object = new HashMap<>();
                    object.put(map.getKey(), map.getValue());
                    excelWriter.fill(object, writeSheet);
                }
            }
        }
        setCal(excelWriter);
        excelWriter.finish();
    }

    private static void setCal(ExcelWriter excelWriter) {
        Workbook workbook = excelWriter.writeContext().writeWorkbookHolder().getWorkbook();
        workbook.setForceFormulaRecalculation(true);
    }

    /**
     * 重置响应体
     */
    private static void resetResponse(String sheetName, HttpServletResponse response) throws UnsupportedEncodingException {
        String filename = encodingFilename(sheetName);
        FileUtils.setAttachmentResponseHeader(response, filename);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
    }

    /**
     * 解析导出值 0=男,1=女,2=未知
     *
     * @param propertyValue 参数值
     * @param converterExp  翻译注解
     * @param separator     分隔符
     * @return 解析后值
     */
    public static String convertByExp(String propertyValue, String converterExp, String separator) {
        StringBuilder propertyString = new StringBuilder();
        String[] convertSource = converterExp.split(StringUtils.SEPARATOR);
        for (String item : convertSource) {
            String[] itemArray = item.split("=");
            if (StringUtils.containsAny(propertyValue, separator)) {
                for (String value : propertyValue.split(separator)) {
                    if (itemArray[0].equals(value)) {
                        propertyString.append(itemArray[1] + separator);
                        break;
                    }
                }
            } else {
                if (itemArray[0].equals(propertyValue)) {
                    return itemArray[1];
                }
            }
        }
        return StringUtils.stripEnd(propertyString.toString(), separator);
    }

    /**
     * 反向解析值 男=0,女=1,未知=2
     *
     * @param propertyValue 参数值
     * @param converterExp  翻译注解
     * @param separator     分隔符
     * @return 解析后值
     */
    public static String reverseByExp(String propertyValue, String converterExp, String separator) {
        StringBuilder propertyString = new StringBuilder();
        String[] convertSource = converterExp.split(StringUtils.SEPARATOR);
        for (String item : convertSource) {
            String[] itemArray = item.split("=");
            if (StringUtils.containsAny(propertyValue, separator)) {
                for (String value : propertyValue.split(separator)) {
                    if (itemArray[1].equals(value)) {
                        propertyString.append(itemArray[0] + separator);
                        break;
                    }
                }
            } else {
                if (itemArray[1].equals(propertyValue)) {
                    return itemArray[0];
                }
            }
        }
        return StringUtils.stripEnd(propertyString.toString(), separator);
    }

    /**
     * 编码文件名
     */
    public static String encodingFilename(String filename) {
        return IdUtil.fastSimpleUUID() + "_" + filename + ".xlsx";
    }

    public static <T> void exportListExcel(String templatePath, List<T> list, HttpServletResponse response, Map<String, String> params) throws IOException {
        List<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> sheet1 = new HashMap<>();
        sheet1.put("list", list);
        for (String key : params.keySet()) {
            sheet1.put(key, params.get(key));
        }
        data.add(sheet1);
        OutputStream os = response.getOutputStream();
        ExcelUtil.exportTemplateMultiSheet(data, templatePath, os);
    }

    public static <T> void exportListExcel(String templatePath, List<T> list, HttpServletResponse response) throws IOException {
        exportListExcel(templatePath, list, response, new HashMap<>());
    }

    public static <T> List<T> importExcel(InputStream is, Class<T> clazz, Integer rowNo) {
        return EasyExcel.read(is).head(clazz).autoCloseStream(false).sheet().headRowNumber(rowNo).doReadSync();
    }

}
