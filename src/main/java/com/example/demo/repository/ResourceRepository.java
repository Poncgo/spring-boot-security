package com.example.demo.repository;

import com.example.demo.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ResourceRepository extends JpaRepository<Resource,Integer> {

    /**
     *  通过角色名称获取资源列表
     * @param rolename
     * @return
     *
     */
    @Query(value = "SELECT * FROM resource  WHERE id IN ( SELECT resource_id FROM resource_role  WHERE role_id = ( SELECT  id  FROM role  WHERE roleName = ?1))",nativeQuery = true)
    List<Resource> findByRoleName(String rolename);
}
