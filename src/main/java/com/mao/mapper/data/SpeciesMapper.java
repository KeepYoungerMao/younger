package com.mao.mapper.data;

import com.mao.entity.species.SpeciesListSrc;
import com.mao.entity.species.SpeciesTree;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 物种数据请求
 * @author mao by 16:47 2019/6/21
 */
@Repository
@Mapper
public interface SpeciesMapper {

    //根据pid获取物种树类信息
    List<SpeciesTree> getSpeciesTreeByPid(@Param("pid") String pid);

    //根据src_id获取物种详情
    SpeciesListSrc getSpeciesListSrcBySrcId(@Param("id") String id);
}