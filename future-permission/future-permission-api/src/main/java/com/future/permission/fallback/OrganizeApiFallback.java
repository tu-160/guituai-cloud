package com.future.permission.fallback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.future.permission.OrganizeApi;
import com.future.permission.entity.OrganizeEntity;
import com.future.permission.model.organize.OrganizeConditionModel;

import java.util.*;

/**
 * 获取组织信息Api降级处理
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-03-24
 */
@Component
@Slf4j
public class OrganizeApiFallback implements OrganizeApi {
    @Override
    public OrganizeEntity getInfoById(String organizeId) {
        return new OrganizeEntity();
    }

    @Override
    public List<OrganizeEntity> getList() {
        return new ArrayList<>(16);
    }

    @Override
    public List<OrganizeEntity> getOrganizeName(List<String> id) {
        return  new ArrayList<>(16);
    }

    @Override
    public OrganizeEntity getByFullName(String fullName) {
        return null;
    }

    @Override
    public List<OrganizeEntity> getOrganizeId(String organizeId) {
        return new ArrayList<>(16);
    }

    @Override
    public String getFullNameByOrgIdTree(String organizeIdTree) {
        return null;
    }

    @Override
    public Map<String, Object> getOrgMap(String type,String category) {
        return new HashMap<>();
    }

    @Override
    public String getOrganizeIdTree(OrganizeEntity organizeEntity) {
        return null;
    }

    @Override
    public void save(OrganizeEntity organizeEntity) {}

    @Override
    public List<OrganizeEntity> getOrganizeByParentId(){
        return new ArrayList<OrganizeEntity>();
    }

    @Override
    public void updateOrganizeEntity(String organizeId, OrganizeEntity organizeEntity){}

    @Override
    public List<String> getUnderOrganizations(String organizeId) {
        return null;
    }

    @Override
    public List<String> upWardRecursion(String organizeId) {
        return new ArrayList();
    }

    @Override
    public Map<String, OrganizeEntity> getOrgMapsAll() {
        return Collections.emptyMap();
    }

    @Override
    public void removeOrganizeInfoList() {
        log.error("清除缓存失败");
    }

    @Override
    public List<OrganizeEntity> getOrganizeDepartmentAll(String organize) {
        return new ArrayList<>();
    }

    @Override
    public String getDefaultCurrentDepartmentId(OrganizeConditionModel organizeConditionModel) {return "";}

    @Override
    public List<OrganizeEntity> getOrganizeChildList(List<String> id) {
        return new ArrayList<>();
    }

    @Override
    public Map<String, String> getInfoList() { return null; }

    @Override
    public Map<String, Object> getAllOrgsTreeName() { return null; }
}
