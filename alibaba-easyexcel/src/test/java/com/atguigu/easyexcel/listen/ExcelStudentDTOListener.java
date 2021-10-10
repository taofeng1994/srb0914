package com.atguigu.easyexcel.listen;


import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.easyexcel.dto.ExcelStudentDTO;

public class ExcelStudentDTOListener extends AnalysisEventListener<ExcelStudentDTO> {
    int a = 1;

    //每读完一行数据就执行一次该方法
    @Override
    public void invoke(ExcelStudentDTO excelStudentDTO, AnalysisContext analysisContext) {
        System.out.println(excelStudentDTO);
        System.out.println("读完了"+ a+"行");
        a++;

    }


    //读完所有数据后执行该方法
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        System.out.println("所有数据已经读完");
    }
}
