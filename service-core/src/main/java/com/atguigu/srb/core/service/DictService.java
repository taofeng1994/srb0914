package com.atguigu.srb.core.service;

import com.atguigu.srb.core.pojo.dto.ExcelDictDTO;
import com.atguigu.srb.core.pojo.entity.Dict;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 数据字典 服务类
 * </p>
 *
 * @author ${author}
 * @since 2021-09-22
 */
public interface DictService extends IService<Dict> {


    void importData(InputStream inputStream);

    List<ExcelDictDTO> listDictData();

    List<Dict> listByParentId(Long parentId);

    List<Dict> fidByDictCode(String dictCode);



}
