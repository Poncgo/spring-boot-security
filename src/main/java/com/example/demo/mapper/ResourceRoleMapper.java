package com.example.demo.mapper;

import com.example.demo.entity.ResourceRole;
import com.example.demo.entity.ResourceRoleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ResourceRoleMapper {
    long countByExample(ResourceRoleExample example);

    int deleteByExample(ResourceRoleExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ResourceRole record);

    int insertSelective(ResourceRole record);

    List<ResourceRole> selectByExample(ResourceRoleExample example);

    ResourceRole selectByPrimaryKey(Integer id);
    
    List<ResourceRole> findByRoleId(Integer id);
    
    List<ResourceRole> fingByResourceId(Integer id);

    int updateByExampleSelective(@Param("record") ResourceRole record, @Param("example") ResourceRoleExample example);

    int updateByExample(@Param("record") ResourceRole record, @Param("example") ResourceRoleExample example);

    int updateByPrimaryKeySelective(ResourceRole record);

    int updateByPrimaryKey(ResourceRole record);
}