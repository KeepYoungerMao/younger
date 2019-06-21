package com.mao.mvc.data;

import com.mao.entity.species.SpeciesListSrc;
import com.mao.entity.species.SpeciesTree;
import com.mao.service.data.SpeciesService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 物种数据请求
 * @author mao by 16:35 2019/6/21
 */
@RestController
@RequestMapping("data/species")
public class SpeciesMvc {

    @Resource
    private SpeciesService speciesService;

    /**
     * 获取物种树类信息
     * @param id pid
     * @return 物种树列表
     */
    @RequestMapping(value = "tree/{id}", method = RequestMethod.GET)
    public List<SpeciesTree> speciesTrees(@PathVariable("id") String id){
        return speciesService.speciesTrees(id);
    }

    /**
     * 获取物种详细信息（列表信息）
     * @param id 物种id
     * @return 物种详情
     */
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public SpeciesListSrc speciesListSrc(@PathVariable("id") String id){
        return speciesService.speciesListSrc(id);
    }

}