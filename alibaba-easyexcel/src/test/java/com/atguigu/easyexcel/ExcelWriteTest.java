package com.atguigu.easyexcel;


import com.alibaba.excel.EasyExcel;
import com.atguigu.easyexcel.dto.ExcelStudentDTO;
import com.atguigu.easyexcel.listen.ExcelStudentDTOListener;
import jdk.management.resource.internal.inst.SocketOutputStreamRMHooks;
import org.junit.Test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

public class ExcelWriteTest {

    //写操作
    @Test
    public void test01() {

        List<ExcelStudentDTO> list = new ArrayList();
        for (int i = 0; i < 10; i++) {
            ExcelStudentDTO excelStudentDTO = new ExcelStudentDTO();
            excelStudentDTO.setName("富贵" + i);
            excelStudentDTO.setSalary(100.0*i);
            excelStudentDTO.setBirthday(new Date());
            list.add(excelStudentDTO);

        }
        File file = new File("d:/excel/simpleWrite.xlsx");


        EasyExcel.write(file, ExcelStudentDTO.class).sheet("测试数据").doWrite(list);


    }


    @Test
    public void read() {
        File file = new File("d:/excel/simpleWrite.xlsx");
        EasyExcel.read(file, ExcelStudentDTO.class,new ExcelStudentDTOListener()).sheet().doRead();
    }






}
