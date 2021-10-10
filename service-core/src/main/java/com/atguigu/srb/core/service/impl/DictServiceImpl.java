package com.atguigu.srb.core.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.srb.core.listener.ExcelDictDTOListener;
import com.atguigu.srb.core.pojo.dto.ExcelDictDTO;
import com.atguigu.srb.core.pojo.entity.Dict;
import com.atguigu.srb.core.mapper.DictMapper;
import com.atguigu.srb.core.service.DictService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 数据字典 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2021-09-22
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Resource
    RedisTemplate redisTemplate;


    @Override
    public void importData(InputStream inputStream) {
        EasyExcel.read(inputStream,ExcelDictDTO.class, new ExcelDictDTOListener(baseMapper)).sheet().doRead();

    }

    @Override
    public List<ExcelDictDTO> listDictData() {
        return baseMapper.listDictData();
    }

    @Override
    public List<Dict> listByParentId(Long parentId) {

        List<Dict> dictList = null;
        try {
            //先查询redis中是否存在数据列表
            dictList = (List<Dict>) redisTemplate.opsForValue().get("srb:core:dictList:" + parentId);
            if (dictList != null) {
                return dictList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //redis缓存中没有数据，从数据库中查询数据
        dictList = baseMapper.selectList(new QueryWrapper<Dict>().eq("parent_id", parentId));

        dictList.forEach(dict -> {
            //如果有子节点，则是非叶子节点
            boolean children = this.hasChildren(dict.getId());
            dict.setHasChildren(children);
        });

        //将查询到的数据存入redis缓存中
        redisTemplate.opsForValue().set("srb:core:dictList:" + parentId, dictList);
        return dictList;
    }


    /**
     * 根据dictCode查询分类
     * @param dictCode
     * @return
     */
    @Override
    public List<Dict> fidByDictCode(String dictCode) {
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<Dict>().eq("dict_code", dictCode);
        Dict dict = baseMapper.selectOne(dictQueryWrapper);
        List<Dict> dictList = new ArrayList<>();
        if (dict != null){
            dictList = this.listByParentId(dict.getId());
        }

        return dictList;
    }

    /**
     * 判断数据字典是否有子节点
     * @param id
     * @return
     */
    private boolean hasChildren(Long id){
        Integer count = baseMapper.selectCount(new QueryWrapper<Dict>().eq("parent_id", id));
        if (count >0){
            return true;
        }
        return false;

    }



















}
