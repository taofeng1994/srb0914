package com.atguigu.srb.core.listener;




import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.srb.core.mapper.DictMapper;
import com.atguigu.srb.core.pojo.dto.ExcelDictDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


public class ExcelDictDTOListener extends AnalysisEventListener<ExcelDictDTO> {

    private static final int BATCH_COUNT = 5;

    private DictMapper dictMapper;

    List<ExcelDictDTO> list = new ArrayList();



    public ExcelDictDTOListener(DictMapper dictMapper) {
        this.dictMapper = dictMapper;
    }

    //每次读完一行数据调用
    @Override
    public void invoke(ExcelDictDTO excelDictDTO, AnalysisContext analysisContext) {
        list.add(excelDictDTO);
        if (list.size() >= BATCH_COUNT) {
            dictMapper.insertBatch(list);
            list.clear();
        }
    }


    //所有数据读完后调用
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        dictMapper.insertBatch(list);

    }
}
