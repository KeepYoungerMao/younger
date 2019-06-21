package com.mao.entity.species;

import lombok.Getter;
import lombok.Setter;

/**
 * 物种列表详情，包含物种与生物分类关系、分布区域等
 * 对应数据库表：tt_species_list_src
 * @author mao by 16:24 2019/6/21
 */
@Getter
@Setter
public class SpeciesListSrc {
    private int id;
    private String src_id;
    private String j_id;
    private String j_name;
    private String m_id;
    private String m_name;
    private String g_id;
    private String g_name;
    private String mu_id;
    private String mu_name;
    private String k_id;
    private String k_name;
    private String s_id;
    private String s_name;
    private String z_id;
    private String z_name;
    private String accepted_name;
    private String chinese_name;
    private String synonym_name;
    private String other_name;
    private String type;
    private String distribute;
}