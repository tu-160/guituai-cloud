package com.future.module.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.future.base.controller.SuperController;
import com.future.common.base.ActionResult;
import com.future.common.base.vo.PageListVO;
import com.future.common.base.vo.PaginationVO;
import com.future.common.constant.MsgCode;
import com.future.common.exception.DataException;
import com.future.common.util.JsonUtil;
import com.future.common.util.StringUtil;
import com.future.common.util.UserProvider;
import com.future.module.system.entity.DataInterfaceEntity;
import com.future.module.system.entity.DataInterfaceLogEntity;
import com.future.module.system.entity.DataInterfaceUserEntity;
import com.future.module.system.entity.InterfaceOauthEntity;
import com.future.module.system.model.InterfaceOauth.*;
import com.future.module.system.model.datainterface.DataInterfaceVo;
import com.future.module.system.service.DataInterfaceLogService;
import com.future.module.system.service.DataInterfaceService;
import com.future.module.system.service.DataInterfaceUserService;
import com.future.module.system.service.InterfaceOauthService;
import com.future.permission.UserApi;
import com.future.permission.entity.UserEntity;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


/**
 * 接口认证控制器
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2022/6/8
 */
@Tag(name = "接口认证", description = "interfaceoauth")
@RestController
@RequestMapping(value = "/InterfaceOauth")
public class InterfaceOauthController extends SuperController<InterfaceOauthService, InterfaceOauthEntity> {
    @Autowired
    private DataInterfaceService dataInterfaceService;
    @Autowired
    private DataInterfaceLogService dataInterfaceLogService;

    @Autowired
    private InterfaceOauthService interfaceOauthService;

    @Autowired
    private UserApi userApi;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private DataInterfaceUserService dataInterfaceUserService;


    /**
     * 获取接口认证列表(分页)
     *
     * @param pagination 分页参数
     * @return ignore
     */
    @Operation(summary = "获取接口认证列表(分页)")
    @SaCheckPermission("systemData.interfaceAuth")
    @GetMapping
    public ActionResult<PageListVO<InterfaceIdentListVo>> getList(PaginationOauth pagination) {
        List<InterfaceOauthEntity> data = interfaceOauthService.getList(pagination);
        List<InterfaceIdentListVo> jsonToList = JsonUtil.getJsonToList(data, InterfaceIdentListVo.class);
        jsonToList.forEach(item -> {
            if (StringUtil.isNotEmpty(userProvider.get().getTenantId())) {
                item.setTenantId(userProvider.get().getTenantId());
            }
            if (item.getCreatorUserId() != null) {
                item.setCreatorUser(userApi.getInfoById(item.getCreatorUserId()).getRealName());
            }
        });
        PaginationVO paginationVO = JsonUtil.getJsonToBean(pagination, PaginationVO.class);
        return ActionResult.page(jsonToList, paginationVO);
    }

    /**
     * 添加接口认证
     *
     * @param interfaceIdentForm 添加接口认证模型
     * @return ignore
     */
    @Operation(summary = "添加接口认证")
    @Parameter(name = "interfaceIdentForm", description = "添加接口认证模型", required = true)
    @SaCheckPermission("systemData.interfaceAuth")
    @PostMapping
    public ActionResult create(@RequestBody @Valid InterfaceIdentForm interfaceIdentForm) {
        InterfaceOauthEntity entity = JsonUtil.getJsonToBean(interfaceIdentForm, InterfaceOauthEntity.class);
        if (interfaceOauthService.isExistByAppName(entity.getAppName(), entity.getId())) {
            return ActionResult.fail(MsgCode.EXIST001.get());
        }
        if (interfaceOauthService.isExistByAppId(entity.getAppId(), entity.getId())) {
            return ActionResult.fail("内容不能重复");
        }
        interfaceOauthService.create(entity);


        return ActionResult.success("接口认证创建成功");
    }


    /**
     * 修改接口认证
     *
     * @param interfaceIdentForm 添加接口认证模型
     * @param id 主键
     * @return ignore
     */
    @Operation(summary = "修改接口认证")
    @Parameters({
            @Parameter(name = "interfaceIdentForm", description = "添加接口认证模型", required = true),
            @Parameter(name = "id", description = "主键", required = true)
    })
    @SaCheckPermission("systemData.interfaceAuth")
    @PutMapping("/{id}")
    public ActionResult update(@RequestBody @Valid InterfaceIdentForm interfaceIdentForm, @PathVariable("id") String id) throws DataException {
        InterfaceOauthEntity entity = JsonUtil.getJsonToBean(interfaceIdentForm, InterfaceOauthEntity.class);
        if (interfaceOauthService.isExistByAppName(entity.getAppName(), id)) {
            return ActionResult.fail(MsgCode.EXIST001.get());
        }
        if (interfaceOauthService.isExistByAppId(entity.getAppId(), id)) {
            return ActionResult.fail("内容不能重复");
        }
        boolean flag = interfaceOauthService.update(entity, id);
        if (flag == false) {
            return ActionResult.fail(MsgCode.FA002.get());
        }
        return ActionResult.success(MsgCode.SU004.get());
    }

    /**
     * 删除接口认证
     *
     * @param id 主键
     * @return
     */
    @Operation(summary = "删除接口")
    @Parameters({
            @Parameter(name = "id", description = "主键", required = true)
    })
    @SaCheckPermission("systemData.interfaceAuth")
    @DeleteMapping("/{id}")
    public ActionResult delete(@PathVariable String id) {
        InterfaceOauthEntity entity = interfaceOauthService.getInfo(id);
        if (entity != null) {
            interfaceOauthService.delete(entity);
            return ActionResult.success(MsgCode.SU003.get());
        }
        return ActionResult.fail(MsgCode.FA003.get());
    }

    /**
     * 获取秘钥
     *
     * @return
     */
    @Operation(summary = "获取接口认证密钥")
    @SaCheckPermission("systemData.interfaceAuth")
    @GetMapping("/getAppSecret")
    public ActionResult getAppSecret() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return ActionResult.success("获取成功", uuid);
    }


    /**
     * 保存綁定认证接口
     *
     * @param identInterfaceListModel 授权接口列表模型
     * @return
     */
    @Operation(summary = "保存綁定认证接口")
    @Parameters({
            @Parameter(name = "identInterfaceListModel", description = "授权接口列表模型", required = true)
    })
    @SaCheckPermission("systemData.interfaceAuth")
    @PostMapping("/saveInterfaceList")
    public ActionResult getInterfaceList(@RequestBody IdentInterfaceListModel identInterfaceListModel) {
        InterfaceOauthEntity entity = new InterfaceOauthEntity();
        entity.setId(identInterfaceListModel.getInterfaceIdentId());
        entity.setDataInterfaceIds(identInterfaceListModel.getDataInterfaceIds());
        boolean b = interfaceOauthService.updateById(entity);
        if (b) {
            return ActionResult.success(MsgCode.SU002.get());
        }
        return ActionResult.success(MsgCode.FA101.get());
    }

    /**
     * 获取接口授权绑定接口列表
     *
     * @param id 主键
     * @return
     */
    @Operation(summary = "获取认证基础信息及接口列表")
    @Parameters({
            @Parameter(name = "id", description = "主键", required = true)
    })
    @SaCheckPermission("systemData.interfaceAuth")
    @GetMapping("/{id}")
    public ActionResult getInterfaceList(@PathVariable("id") String id) {
        InterfaceOauthEntity entity = interfaceOauthService.getInfo(id);
        InterfaceIdentVo bean = JsonUtil.getJsonToBean(entity, InterfaceIdentVo.class);
        if (StringUtils.isNotEmpty(bean.getDataInterfaceIds())) {
            List<DataInterfaceVo> listDataInterfaceVo = new ArrayList<>();
            List<DataInterfaceEntity> list = dataInterfaceService.getList(false);
            list.forEach(item -> {
                if (bean.getDataInterfaceIds().contains(item.getId())) {
                    DataInterfaceVo dataInterfaceVo = JsonUtil.getJsonToBean(item, DataInterfaceVo.class);
                    listDataInterfaceVo.add(dataInterfaceVo);
                }
            });
            bean.setList(listDataInterfaceVo);
        }

        //添加授权用户信息
        List<InterfaceUserVo> listIuv =new ArrayList<>();
        List<DataInterfaceUserEntity> select = dataInterfaceUserService.select(id);
        for(DataInterfaceUserEntity diue:select){
            String userId = diue.getUserId();
            UserEntity info = userApi.getInfoById(userId);
            InterfaceUserVo iuv=new InterfaceUserVo();
            iuv.setUserId(userId);
            iuv.setUserKey(diue.getUserKey());
            iuv.setUserName(info.getRealName()+"/"+info.getAccount());
            listIuv.add(iuv);
        }
        bean.setUserList(listIuv);
        return ActionResult.success("获取成功", bean);
    }

    /**
     * 获取日志列表
     *
     * @param id 主键
     * @param paginationIntrfaceLog 分页参数
     * @return
     */
    @Operation(summary = "获取日志列表")
    @Parameters({
            @Parameter(name = "id", description = "主键", required = true)
    })
    @SaCheckPermission("systemData.interfaceAuth")
    @GetMapping("/dataInterfaceLog/{id}")
    public ActionResult<PageListVO<IdentDataInterfaceLogVO>> getInterfaceList(@PathVariable("id") String id,PaginationIntrfaceLog paginationIntrfaceLog) {
        InterfaceOauthEntity entity = interfaceOauthService.getInfo(id);
        List<IdentDataInterfaceLogVO> voList = null;
        PaginationVO vo = null;
        if (entity!=null&&StringUtils.isNotEmpty(entity.getDataInterfaceIds())) {
            String dataInterfaceIds = entity.getDataInterfaceIds();
            String[] split = dataInterfaceIds.split(",");
            List<String> list = Arrays.asList(split);
            List<DataInterfaceLogEntity> listByIds = dataInterfaceLogService.getListByIds(entity.getAppId(),list, paginationIntrfaceLog);
            voList = JsonUtil.getJsonToList(listByIds, IdentDataInterfaceLogVO.class);
            List<DataInterfaceEntity> listDataInt = dataInterfaceService.getList(false);
            for (IdentDataInterfaceLogVO invo : voList) {
                if (StringUtil.isNotEmpty(userProvider.get().getTenantId())) {
                    invo.setTenantId(userProvider.get().getTenantId());
                }
                //绑定用户
                UserEntity userEntity = userApi.getInfoById(invo.getUserId());
                if (userEntity != null) {
                    invo.setUserId(userEntity.getRealName() + "/" + userEntity.getAccount());
                }
                //绑定接口基础数据
                listDataInt.forEach(item -> {
                    if (invo.getInvokId().contains(item.getId())) {
                        DataInterfaceVo dataInterfaceVo = JsonUtil.getJsonToBean(item, DataInterfaceVo.class);
                        invo.setFullName(dataInterfaceVo.getFullName());
                        invo.setEnCode(dataInterfaceVo.getEnCode());
                    }
                });
            }
            vo = JsonUtil.getJsonToBean(paginationIntrfaceLog, PaginationVO.class);

        }
        return ActionResult.page(voList, vo);
    }

    @Operation(summary = "授权用户")
    @SaCheckPermission("systemData.interfaceAuth")
    @PostMapping("/SaveUserList")
    public ActionResult saveUserList(@RequestBody InterfaceUserForm interfaceUserForm) {
        dataInterfaceUserService.saveUserList(interfaceUserForm);
        return ActionResult.success(MsgCode.SU002.get());
    }
}
