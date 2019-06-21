package com.mao.entity.species;

import lombok.Getter;
import lombok.Setter;

/**
 * 物种树结构，界门纲目科属种对应的上下级树形结构
 * 对应数据库表：tt_species_tree
 * @author mao by 16:27 2019/6/21
 */
@Getter
@Setter
public class SpeciesTree {
    private String id;
    private String pid;
    private boolean isParent;
    private String name;
    private String pinyin;
    private String url;
}