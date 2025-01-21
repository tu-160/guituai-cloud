package com.future.module.oauth.service.impl;

import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.future.common.base.*;
import com.future.common.base.entity.*;
import com.future.common.config.FutureOauthConfig;
import com.future.common.constant.PlatformConst;
import com.future.common.constant.MsgCode;
import com.future.common.constant.PermissionConst;
import com.future.common.exception.LoginException;
import com.future.common.granter.UserDetailsServiceBuilder;
import com.future.common.model.BaseSystemInfo;
import com.future.common.model.login.*;
import com.future.common.model.tenant.TenantAuthorizeModel;
import com.future.common.model.tenant.TenantVO;
import com.future.common.properties.SecurityProperties;
import com.future.common.service.LoginService;
import com.future.common.util.*;
import com.future.common.util.treeutil.ListToTreeUtil;
import com.future.common.util.treeutil.SumTree;
import com.future.common.util.treeutil.newtreeutil.TreeDotUtils;
import com.future.database.util.TenantDataSourceUtil;
import com.future.file.util.UploaderUtil;
import com.future.module.message.MessageTemplateConfigApi;
import com.future.module.message.SentMessageApi;
import com.future.module.message.entity.MessageTemplateConfigEntity;
import com.future.module.oauth.model.BuildUserCommonInfoModel;
import com.future.module.oauth.utils.LoginHolder;
import com.future.module.system.ModuleApi;
import com.future.module.system.ModuleButtonApi;
import com.future.module.system.ModuleColumnApi;
import com.future.module.system.ModuleDataAuthorizeSchemeApi;
import com.future.module.system.ModuleFormApi;
import com.future.module.system.SysConfigApi;
import com.future.module.system.SystemApi;
import com.future.module.system.entity.ModuleButtonEntity;
import com.future.module.system.entity.ModuleColumnEntity;
import com.future.module.system.entity.ModuleDataAuthorizeSchemeEntity;
import com.future.module.system.entity.ModuleEntity;
import com.future.module.system.entity.ModuleFormEntity;
import com.future.module.system.entity.SystemEntity;
import com.future.module.system.model.base.SystemBaeModel;
import com.future.module.system.model.button.ButtonModel;
import com.future.module.system.model.column.ColumnModel;
import com.future.module.system.model.form.ModuleFormModel;
import com.future.module.system.model.module.ModuleApiByIdsModel;
import com.future.module.system.model.module.ModuleModel;
import com.future.module.system.model.resource.ResourceModel;
import com.future.permission.*;
import com.future.permission.entity.*;
import com.future.permission.model.authorize.AuthorizeVO;
import com.future.permission.model.organizerelation.AutoGetMajorOrgIdModel;
import com.future.permission.model.user.UserUpdateModel;
import com.future.permission.service.SignService;
import com.future.reids.config.ConfigValueUtil;
import com.future.reids.util.RedisUtil;
import com.future.security.permissions.PermissionInterfaceImpl;
import com.future.visualdev.portal.PortalApi;
import com.future.visualdev.portal.constant.PortalConst;
import com.google.common.collect.ImmutableMap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.future.common.util.Constants.ADMIN_KEY;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021/3/16
 */
@Slf4j
@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private ConfigValueUtil configValueUtil;
    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private UserProvider userProvider;
    @Autowired
    private UserApi userApi;
    @Autowired
    private UserRelationApi userRelationApi;
    @Autowired
    private GroupApi groupApi;
    @Autowired
    private OrganizeApi organizeApi;
    @Autowired
    private PositionApi positionApi;
    @Autowired
    private RoleApi roleApi;
    @Autowired
    private AuthorizeApi authorizeApi;
    @Autowired
    private SysConfigApi sysconfigApi;
    @Autowired
    private PortalApi portalApi;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private CacheKeyUtil cacheKeyUtil;
    @Autowired
    private OrganizeRelationApi organizeRelationApi;
    @Autowired
    private SystemApi systemApi;
    @Autowired
    private UserDetailsServiceBuilder userDetailsServiceBuilder;
    @Autowired
    private SignService signService;
    @Autowired
    private SentMessageApi sentMessageApi;
    @Autowired
    private MessageTemplateConfigApi messageTemplateApi;
    @Autowired
    private OrganizeAdminTratorApi organizeAdminTratorApi;
    @Autowired
    private ModuleApi moduleApi;
    @Autowired
    private PermissionGroupApi permissionGroupApi;
    @Autowired
    private ModuleButtonApi buttonApi;
    @Autowired
    private ModuleColumnApi columnApi;
    @Autowired
    private ModuleFormApi formApi;
    @Autowired
    private ModuleDataAuthorizeSchemeApi dataAuthorizeSchemeApi;
    @Autowired
    private FutureOauthConfig futureOauthConfig;


    @Override
    public UserInfo getTenantAccount(UserInfo userInfo) throws LoginException {
        String tenantId = "";
        if (configValueUtil.isMultiTenancy()) {
            String[] tenantAccount = userInfo.getUserAccount().split("\\@");
            tenantId = tenantAccount.length == 1 ? userInfo.getUserAccount() : tenantAccount[0];
            userInfo.setUserAccount(tenantAccount.length == 1 ? ADMIN_KEY : tenantAccount[1]);
            if (StringUtil.isEmpty(tenantId) && ServletUtil.getRequest() != null) {
                String remoteHost = ServletUtil.getRequest().getRemoteHost();
                if (ObjectUtil.equal(UrlBuilder.of(futureOauthConfig.getFutureDomain()).getHost(), remoteHost)) {
                    tenantId = remoteHost.split("\\.")[0];
                }
            }
            if (tenantAccount.length > 2 || StringUtil.isEmpty(userInfo.getUserAccount())) {
                throw new LoginException(MsgCode.LOG102.get());
            }
            TenantVO tenantVO = TenantDataSourceUtil.getRemoteTenantInfo(tenantId);
            TenantDataSourceUtil.switchTenant(tenantId, tenantVO);
            //切换成租户库
            userInfo.setTenantId(tenantId);
            userInfo.setTenantDbConnectionString(tenantVO.getDbName());
            userInfo.setTenantDbType(tenantVO.getType());
            //查库测试
            BaseSystemInfo baseSystemInfo = null;
            try {
                baseSystemInfo = getBaseSystemConfig(userInfo.getTenantId());
            }catch (Exception e) {
                log.error("登录获取系统配置失败: {}", e.getMessage());
            }
            if(baseSystemInfo == null || baseSystemInfo.getSingleLogin() == null) {
                if (configValueUtil.getMultiTenancyUrl().contains("https")) {
                    throw new LoginException("租户登录失败，请用手机验证码登录");
                } else {
                    throw new LoginException("数据库异常，请联系管理员处理");
                }
            }
        }
        return userInfo;
    }

    @Override
    public UserInfo userInfo(UserInfo userInfo, BaseSystemInfo sysConfigInfo) throws LoginException {
        //获取账号信息
        UserEntity userEntity = LoginHolder.getUserEntity();
        if(userEntity == null){
            userEntity = userDetailsServiceBuilder.getUserDetailService(userInfo.getUserDetailKey()).loadUserEntity(userInfo);
            LoginHolder.setUserEntity(userEntity);
        }

        checkUser(userEntity, userInfo, sysConfigInfo);

        userInfo.setIsAdministrator(BooleanUtil.toBoolean(String.valueOf(userEntity.getIsAdministrator())));
        userInfo.setUserId(userEntity.getId());
        userInfo.setUserAccount(userEntity.getAccount());
        userInfo.setUserName(userEntity.getRealName());
        userInfo.setUserIcon(userEntity.getHeadIcon());
        userInfo.setTheme(userEntity.getTheme());
        userInfo.setOrganizeId(userEntity.getOrganizeId());
        userInfo.setPortalId(userEntity.getPortalId());
        userInfo.setIsAdministrator(BooleanUtil.toBoolean(String.valueOf((userEntity.getIsAdministrator()))));

        // 添加过期时间
        String time = sysConfigInfo.getTokenTimeout();
        if (StringUtil.isNotEmpty(time)) {
            Integer minu = Integer.valueOf(time);
            userInfo.setOverdueTime(DateUtil.dateAddMinutes(null, minu));
            userInfo.setTokenTimeout(minu);
        }

        String ipAddr = IpUtil.getIpAddr();
        userInfo.setLoginIpAddress(ipAddr);
        userInfo.setLoginIpAddressName(IpUtil.getIpCity(ipAddr));
        userInfo.setLoginTime(DateUtil.getmmNow());
        UserAgent userAgent = UserAgentUtil.parse(ServletUtil.getUserAgent());
        if (userAgent != null) {
            userInfo.setLoginPlatForm(userAgent.getPlatform().getName() + " " + userAgent.getOsVersion());
            userInfo.setBrowser(userAgent.getBrowser().getName() + " " + userAgent.getVersion());
        }
        userInfo.setPrevLoginTime(userEntity.getPrevLogTime());
        userInfo.setPrevLoginIpAddress(userEntity.getPrevLogIp());
        userInfo.setPrevLoginIpAddressName(IpUtil.getIpCity(userEntity.getPrevLogIp()));
        // 生成id
        String token = RandomUtil.uuId();
        userInfo.setId(cacheKeyUtil.getLoginToken(userInfo.getTenantId()) + token);

        createUserOnline(userInfo);
        return userInfo;
    }

    @Override
    public void updatePasswordMessage(){
        UserInfo userInfo = userProvider.get();
        UserEntity userEntity = userApi.getInfoById(userInfo.getUserId());
        BaseSystemInfo baseSystemInfo = sysconfigApi.getSysConfigInfo();
        if(baseSystemInfo.getPasswordIsUpdatedRegularly()==1){
            Date changePasswordDate = userEntity.getCreatorTime();
            if(userEntity.getChangePasswordDate()!=null){
                changePasswordDate = userEntity.getChangePasswordDate();
            }
            //当前时间
            Date nowDate = DateUtil.getNowDate();
            //更新周期
            Integer updateCycle = baseSystemInfo.getUpdateCycle();
            //提前N天提醒
            Integer updateInAdvance = baseSystemInfo.getUpdateInAdvance();
            Integer day = DateUtil.getDiffDays(changePasswordDate,nowDate);
            if(day>=(updateCycle-updateInAdvance)){
                MessageTemplateConfigEntity entity = messageTemplateApi.getInfoByEnCode("XTXXTX001");
                if(entity != null) {
                    List<String> toUserIds = new ArrayList<>();
                    toUserIds.add(userInfo.getUserId());
                    sentMessageApi.sentMessage(toUserIds, entity.getTitle(), entity.getContent(), Integer.parseInt(entity.getMessageSource()),Integer.parseInt(entity.getMessageType()), userInfo);
                }
            }
        }
    }

    /**
     * 创建用户在线信息
     * @param userInfo
     */
    private void createUserOnline(UserInfo userInfo){
        String userId = userInfo.getUserId();
//        long time= DateUtil.getTime(userInfo.getOverdueTime()) - DateUtil.getTime(new Date());

        String authorize = String.valueOf(redisUtil.getString(cacheKeyUtil.getUserAuthorize() + userId));
//        String loginOnlineKey=cacheKeyUtil.getLoginOnline() + userId;
        redisUtil.remove(authorize);
        //记录Token
//        redisUtil.insert(userInfo.getId(), userInfo,time);
        //记录在线
        if (ServletUtil.getIsMobileDevice()) {
//            redisUtil.insert(cacheKeyUtil.getMobileLoginOnline() + userId, userInfo.getId(), time);
            //记录移动设备CID,用于消息推送
            if (ServletUtil.getHeader("clientId") != null) {
                String clientId = ServletUtil.getHeader("clientId");
                Map<String, String> map = new HashMap<>(16);
                map.put(userInfo.getUserId(), clientId);
                redisUtil.insert(cacheKeyUtil.getMobileDeviceList(), map);
            }
        } else {
//            redisUtil.insert(loginOnlineKey, userInfo.getId(), time);
        }
    }

    private UserCommonInfoVO data(BuildUserCommonInfoModel buildUserCommonInfoModel) {
        UserInfo userInfo = buildUserCommonInfoModel.getUserInfo();
        //公司Id
//        List<OrganizeEntity> list = organizeApi.getList(false);
        UserEntity userEntity = buildUserCommonInfoModel.getUserEntity();
        userInfo.setManagerId(userInfo.getManagerId());
        boolean b = userInfo.getIsAdministrator();
        if (StringUtil.isEmpty(userEntity.getSystemId())) {
            SystemEntity systemEntity = buildUserCommonInfoModel.getMainSystemEntity();
            userInfo.setSystemId(systemEntity.getId());
            userEntity.setSystemId(systemEntity.getId());
        }
        if (StringUtil.isEmpty(userEntity.getAppSystemId())) {
            SystemEntity systemEntity = buildUserCommonInfoModel.getWorkSystemEntity();
            userInfo.setAppSystemId(systemEntity.getId());
            userEntity.setAppSystemId(systemEntity.getId());
        }
        this.userInfo(userInfo, userInfo.getUserId(), b, userEntity, buildUserCommonInfoModel.getSystemId());
//        userInfo.setSubOrganizeIds(this.getSubOrganizeIds(list, userInfo.getOrganizeId(), b));
        List<String> subordinateIdsList = userApi.getListByManagerId(userInfo.getUserId()).stream().map(UserEntity::getId).collect(Collectors.toList());
        userInfo.setSubordinateIds(subordinateIdsList);
        userInfo.setLoginTime(DateUtil.getmmNow());
//        if (StringUtil.isNotEmpty(userInfo.getId())) {
//            redisUtil.insert(userInfo.getId(), userInfo, DateUtil.getTime(userInfo.getOverdueTime()) - DateUtil.getTime(new Date()));
//        }
        BaseSystemInfo baseSystemInfo = buildUserCommonInfoModel.getBaseSystemInfo();
        UserCommonInfoVO infoVO = JsonUtil.getJsonToBean(genUserInfo(userInfo, baseSystemInfo), UserCommonInfoVO.class);
        infoVO.setGroupIds(userInfo.getGroupIds());
        infoVO.setGroupNames(userInfo.getGroupNames());
        // 角色数组
        infoVO.setRoleIds(userInfo.getRoleIds());
        //最后一次修改密码时间
        infoVO.setChangePasswordDate(userEntity.getChangePasswordDate());
        // 角色名称
        StringBuilder roleName = new StringBuilder();
        for (RoleEntity entity : roleApi.getListByIds(userInfo.getRoleIds())) {
            roleName.append("," + entity.getFullName());
        }
        if (roleName.length() > 0) {
            infoVO.setRoleName(roleName.toString().replaceFirst(",", ""));
        }
        // 主管
        UserEntity info = userApi.getInfoById(userEntity.getManagerId());
        if (info != null) {
            infoVO.setManager(info.getRealName() + "/" + info.getAccount());
        }
        // 手机
        infoVO.setMobilePhone(userEntity.getMobilePhone());
        // 邮箱
        infoVO.setEmail(userEntity.getEmail());
        // 生日
        infoVO.setBirthday(userEntity.getBirthday() != null ? userEntity.getBirthday().getTime() : null);
        // 姓名
        infoVO.setUserName(userEntity.getRealName());
        //组织
        OrganizeEntity organizeEntity = organizeApi.getInfoById(userInfo.getOrganizeId());
        String organizeName = null;
        String departmentId = null;
        String departmentName = null;
        List<String> departmentIdList = null;
        String organizeId = null;
        if (organizeEntity != null) {
            if (PermissionConst.DEPARTMENT.equals(organizeEntity.getCategory())) {
                organizeName = organizeEntity.getFullName();
                organizeId = organizeEntity.getId();
            }
            if (StringUtil.isNotEmpty(organizeEntity.getOrganizeIdTree())) {
                String[] split = organizeEntity.getOrganizeIdTree().split(",");
                departmentId = split.length > 0 ? split[split.length - 1] : "";
                departmentIdList = split.length > 0 ? Arrays.asList(split) : new ArrayList<String>();
                departmentName = organizeApi.getFullNameByOrgIdTree(organizeEntity.getOrganizeIdTree());
            }
        }
        userInfo.setOrganize(departmentName);
        infoVO.setOrganizeName(departmentName);
        infoVO.setOrganizeId(departmentId);
        infoVO.setOrganizeIdList(departmentIdList == null?new ArrayList<String>():departmentIdList);
        // 部门id
        infoVO.setDepartmentId(organizeId);
        // 部门名称
        infoVO.setDepartmentName(organizeName);
        infoVO.setIsAdministrator(BooleanUtil.toBoolean(String.valueOf(userEntity.getIsAdministrator())));

        return infoVO;
    }

//    /**
//     * 得到系统模型
//     *
//     * @param userEntity
//     */
//    private void getSystemVO(UserInfo userInfo, UserEntity userEntity, List<UserSystemVO> systemIds) {
//        List<String> currentUserSystem = systemApi.getCurrentUserSystem(userInfo);
//        if (currentUserSystem.size() > 0) {
//            List<SystemEntity> list1 = systemApi.getListByIds(currentUserSystem);
//            list1.forEach(t -> {
//                UserSystemVO userSystemVO = new UserSystemVO();
//                userSystemVO.setId(t.getId());
//                userSystemVO.setName(t.getFullName());
//                userSystemVO.setIcon(t.getIcon());
//                String systemId = userEntity.getSystemId();
//                if (StringUtil.isEmpty(systemId)) {
//                    SystemEntity mainSystem = systemApi.getInfoByEnCode(PlatformConst.MAIN_SYSTEM_CODE);
//                    if (mainSystem.getId().equals(t.getId())) {
//                        userSystemVO.setCurrentSystem(true);
//                        userInfo.setSystemId(mainSystem.getId());
//                    }
//                } else if (t.getId().equals(userEntity.getSystemId())) {
//                    userSystemVO.setCurrentSystem(true);
//                    userInfo.setSystemId(t.getId());
//                }
//                systemIds.add(userSystemVO);
//            });
//        }
//    }

    /**
     * 递归找他的上级
     */
    public void getOrganizeName(List<OrganizeEntity> OrganizeList, String organizeId) throws Exception {
        List<OrganizeEntity> OrganizeList2 = OrganizeList.stream().filter(t -> organizeId.equals(t.getId())).collect(Collectors.toList());
        if (OrganizeList2.size() > 0) {
            for (OrganizeEntity organizeEntity : OrganizeList2) {
                if (organizeEntity.getParentId().equals("-1")) {
                    //父级为-1时候退出
                    throw new Exception(JSON.toJSONString(organizeEntity));
                }
            }
            for (OrganizeEntity orgSub : OrganizeList2) {
                getOrganizeName(OrganizeList, orgSub.getParentId());
            }
        }
    }

    public UserEntity checkUser(UserEntity userEntity, UserInfo userInfo, BaseSystemInfo sysConfigInfo) throws LoginException {
        if (userEntity == null) {
            throw new LoginException(MsgCode.LOG101.get());
        }
        //判断是否组织、岗位、角色、部门主管是否为空，为空则抛出异常
        //判断是否为管理员，是否为Admin(Admin为最高账号，不受限制)
        if (!ADMIN_KEY.equals(userEntity.getAccount()) || userEntity.getIsAdministrator() != 1) {
            //组织id为空则直接抛出异常
            if (StringUtil.isEmpty(userEntity.getOrganizeId())) {
                throw new LoginException(MsgCode.LOG004.get());
            }
            // 岗位id为空则直接抛出异常
//            if (StringUtil.isEmpty(userEntity.getPositionId())) {
//                throw new LoginException("账号异常，请联系管理员修改所属岗位信息");
//            }
//            //角色id为空则直接抛出异常
//            if (StringUtil.isEmpty(userEntity.getRoleId())) {
//                throw new LoginException("账号异常，请联系管理员修改角色信息");
//            }
//            //主管id为空则直接抛出异常
//            if (StringUtil.isEmpty(userEntity.getManagerId())) {
//                throw new LoginException("账号异常，请联系管理员修改主管信息");
//            }
        }
        if (userEntity.getIsAdministrator() == 0) {
            if (userEntity.getEnabledMark() == null) {
                throw new LoginException(MsgCode.LOG005.get());
            }
            if (userEntity.getEnabledMark() == 0) {
                throw new LoginException(MsgCode.LOG006.get());
            }
        }
        if (userEntity.getDeleteMark() != null && userEntity.getDeleteMark() == 1) {
            throw new LoginException(MsgCode.LOG007.get());
        }
        //安全验证
        String ipAddr = IpUtil.getIpAddr();
        userInfo.setLoginIpAddress(IpUtil.getIpAddr());
        // 判断白名单
        if (!ADMIN_KEY.equals(userEntity.getAccount()) && "1".equals(sysConfigInfo.getWhitelistSwitch())) {
            List<String> ipList = Arrays.asList(sysConfigInfo.getWhitelistIp().split(","));
            if (!ipList.contains(ipAddr)) {
                throw new LoginException(MsgCode.LOG010.get());
            }
        }
        //判断用户所属的角色是否被禁用
        if (userEntity.getIsAdministrator() == 0 &&
                organizeAdminTratorApi.getInfoByUserId(userEntity.getId(), userInfo.getTenantId()).size() == 0
        ) {
            List<PermissionGroupEntity> permissionGroupByUserIdAndTenantId = permissionGroupApi.getPermissionGroupByUserIdAndTenantId(userEntity.getId(), userInfo.getTenantId(), null);
            if (permissionGroupByUserIdAndTenantId.size() == 0) {
                throw new LoginException("该用户未分配权限");
            }
            // 如果只有组织权限的话就切换到有权限的组织
            String organizeIdByUserIdAndTenantId = permissionGroupApi.getOrganizeIdByUserIdAndTenantId(userEntity.getId(), userInfo.getTenantId());
            if (StringUtil.isNotEmpty(organizeIdByUserIdAndTenantId)) {
                userEntity.setOrganizeId(organizeIdByUserIdAndTenantId);
            }

//            if (userEntity.getIsAdministrator() == 0) {
//                List<RoleEntity> userAllRole = roleApi.getListByUserId(new RoleInfoModel(userEntity.getId(), userInfo.getTenantId(), userInfo.getTenantDbConnectionString(), userInfo.isAssignDataSource()));
//                boolean permissionFlag = false;
//                for (RoleEntity role : userAllRole) {
//                    if (role != null && role.getEnabledMark() != null && role.getEnabledMark() != 0) {
//                        permissionFlag = true;
//                        break;
//                    }
//                }
//                if(!permissionFlag){
//                    throw new LoginException(MsgCode.LOG011.get());
//                }
//            } else {
//                throw new LoginException(MsgCode.LOG011.get());
//            }
        }
        // 判断当前账号是否被锁定
        Integer lockMark = userEntity.getEnabledMark();
        if (Objects.nonNull(lockMark) && lockMark == 2) {
            // 获取解锁时间
            Date unlockTime = userEntity.getUnlockTime();
            // 账号锁定
            if (sysConfigInfo.getLockType() == 1 || Objects.isNull(unlockTime)) {
                throw new LoginException(MsgCode.LOG012.get());
            }
            // 延迟登陆锁定
            long millis = System.currentTimeMillis();
            // 系统设置的错误次数
            int passwordErrorsNumber = sysConfigInfo.getPasswordErrorsNumber() != null ? sysConfigInfo.getPasswordErrorsNumber() : 0;
            // 用户登录错误次数
            int logErrorCount = userEntity.getLogErrorCount() != null ? userEntity.getLogErrorCount() : 0;
            if (unlockTime.getTime() > millis) {
                // 转成分钟
                int time = (int) ((unlockTime.getTime() - millis) / (1000 * 60));
                throw new LoginException(MsgCode.LOG108.get().replace("{time}", Integer.toString(time + 1)));
            } else if (unlockTime.getTime() < millis && logErrorCount >= passwordErrorsNumber){
                // 已经接触错误时间锁定的话就重置错误次数
                userEntity.setLogErrorCount(0);
                userEntity.setEnabledMark(1);
                userApi.updateById(new UserUpdateModel(userEntity, userInfo.getTenantId()));
            }
        }
        return userEntity;
    }

    /**
     * 获取用户登陆信息
     *
     * @return
     */
    @Override
    public PcUserVO getCurrentUser(String type, String systemCode) {
        UserInfo userInfo = userProvider.get();

        SystemEntity mainSystemEntity = systemApi.getInfoByEnCode(PlatformConst.MAIN_SYSTEM_CODE);
        SystemEntity workSystemEntity = systemApi.getInfoByEnCode(PlatformConst.WORK_SYSTEM_CODE);
        UserEntity userEntity = userApi.getInfoById(userInfo.getUserId());
        if (userEntity == null) {
            return null;
        }
        SystemEntity systemCodeEntity = systemApi.getInfoByEnCode(systemCode);
        if (StringUtil.isNotEmpty(systemCode)) {
            userInfo.setSystemCode(systemCode);
            if ("App".equals(type)) {
                throw new LoginException("仅支持PC端访问，APP端不支持。");
            }
            if (systemCodeEntity == null) {
                UserProvider.logout();
                throw new LoginException("应用不存在");
            } else if (ObjectUtil.equal(systemCodeEntity.getEnabledMark(), 0)) {
                UserProvider.logout();
                throw new LoginException("当前应用已被禁用");
            }
        }
        BaseSystemInfo baseSystemInfo = sysconfigApi.getSysConfigInfo();

        BuildUserCommonInfoModel buildUserCommonInfoModel = new BuildUserCommonInfoModel(userInfo, mainSystemEntity, workSystemEntity, userEntity, baseSystemInfo, Optional.ofNullable(systemCodeEntity).isPresent() ? systemCodeEntity.getId() : null);
        UserCommonInfoVO infoVO = this.data(buildUserCommonInfoModel);
        // 更新userInfo对象
        if (StringUtil.isNotEmpty(userInfo.getId())) {
            UserProvider.setLoginUser(userInfo);
            UserProvider.setLocalLoginUser(userInfo);
        }
        AuthorizeVO authorizeModel = authorizeApi.getAuthorizeByUser(false);
        List<SystemBaeModel> systemList = authorizeModel.getSystemList();

        // 从分管中获取菜单
        List<OrganizeAdministratorEntity> listByUserId1 = organizeAdminTratorApi.getListByUserId(userInfo.getUserId(), PermissionConst.MODULE);
        List<ModuleEntity> moduleEntities = moduleApi.getModuleByIds(new ModuleApiByIdsModel(listByUserId1.stream().map(OrganizeAdministratorEntity::getOrganizeId).collect(Collectors.toList()), null, null, false));
        if ("App".equals(type)) {
            moduleEntities = moduleEntities.stream().filter(t -> !mainSystemEntity.getId().equals(t.getSystemId())).collect(Collectors.toList());
            systemList = systemList.stream().filter(t -> !mainSystemEntity.getId().equals(t.getId())).collect(Collectors.toList());
        } else {
            if (moduleEntities.size() > 0) {
                SystemBaeModel systemBaeModel = JsonUtil.getJsonToBean(mainSystemEntity, SystemBaeModel.class);
                systemList.add(systemBaeModel);
                systemList = systemList.stream().distinct().collect(Collectors.toList());
            }
        }

        List<ModuleModel> moduleJsonToList = JsonUtil.getJsonToList(moduleEntities, ModuleModel.class);

        // 获取菜单权限
        List<ModuleModel> moduleList = authorizeModel.getModuleList();
        moduleList.addAll(moduleJsonToList);
        moduleList = moduleList.stream().distinct().collect(Collectors.toList());

        authorizeModel.setModuleList(moduleList);
        List<ModuleModel> moduleList1 = new ArrayList<>();
        List<ModuleModel> menuList = moduleList.stream().filter(t -> type.equals(t.getCategory())).sorted(Comparator.comparing(ModuleModel::getSortCode)).collect(Collectors.toList());
        moduleList1.addAll(moduleList);

        //岗位
        List<String> posiList = Arrays.asList(userInfo.getPositionIds());
        List<PositionEntity> positionList = positionApi.getPositionName(posiList, false);
        List<UserPositionVO> positionVO = new ArrayList<>();
        for (PositionEntity positionEntity : positionList) {
            UserPositionVO userPositionVO = new UserPositionVO();
            userPositionVO.setName(positionEntity.getFullName());
            userPositionVO.setId(positionEntity.getId());
            positionVO.add(userPositionVO);
        }
        List<PermissionModel> models = new ArrayList<>();

        // 按钮等权限增加分级管理的
        // 按钮
        List<ButtonModel> buttonList = authorizeModel.getButtonList();
        List<ModuleButtonEntity> buttonByModuleId = buttonApi.getListByModuleIds(moduleJsonToList.stream().map(ModuleModel::getId).collect(Collectors.toList()));
        List<ButtonModel> buttonJsonToList = JsonUtil.getJsonToList(buttonByModuleId, ButtonModel.class);
        buttonList.addAll(buttonJsonToList);
        buttonList = buttonList.stream().distinct().collect(Collectors.toList());
        // 列表
        List<ColumnModel> columnList = authorizeModel.getColumnList();
        List<ModuleColumnEntity> columnByModuleId = columnApi.getListByModuleId(moduleJsonToList.stream().map(ModuleModel::getId).collect(Collectors.toList()));
        List<ColumnModel> columnJsonToList = JsonUtil.getJsonToList(columnByModuleId, ColumnModel.class);
        columnList.addAll(columnJsonToList);
        columnList = columnList.stream().distinct().collect(Collectors.toList());
        // 表单
        List<ModuleFormModel> formsList = authorizeModel.getFormsList();
        List<ModuleFormEntity> formByModuleId = formApi.getListByModuleId(moduleJsonToList.stream().map(ModuleModel::getId).collect(Collectors.toList()));
        List<ModuleFormModel> formJsonToList = JsonUtil.getJsonToList(formByModuleId, ModuleFormModel.class);
        formsList.addAll(formJsonToList);
        formsList = formsList.stream().distinct().collect(Collectors.toList());
        // 数据
        List<ResourceModel> resourceList = authorizeModel.getResourceList();
        List<ModuleDataAuthorizeSchemeEntity> resourceByModuleId = dataAuthorizeSchemeApi.getListByModuleId(moduleJsonToList.stream().map(ModuleModel::getId).collect(Collectors.toList()));
        List<ResourceModel> resourceJsonToList = JsonUtil.getJsonToList(resourceByModuleId, ResourceModel.class);
        resourceList.addAll(resourceJsonToList);
        resourceList = resourceList.stream().distinct().collect(Collectors.toList());
        authorizeModel.setButtonList(buttonList);
        authorizeModel.setColumnList(columnList);
        authorizeModel.setFormsList(formsList);
        authorizeModel.setResourceList(resourceList);
        for (ModuleModel moduleModel : menuList) {
            PermissionModel model = new PermissionModel();
            model.setModelId(moduleModel.getId());
            model.setModuleName(moduleModel.getFullName());

            List<ButtonModel> buttonModels = authorizeModel.getButtonList().stream().filter(t -> moduleModel.getId().equals(t.getModuleId())).collect(Collectors.toList());
            List<ColumnModel> columnModels = authorizeModel.getColumnList().stream().filter(t -> moduleModel.getId().equals(t.getModuleId())).collect(Collectors.toList());
            List<ResourceModel> resourceModels = authorizeModel.getResourceList().stream().filter(t -> moduleModel.getId().equals(t.getModuleId())).collect(Collectors.toList());
            List<ModuleFormModel> moduleFormModels = authorizeModel.getFormsList().stream().filter(t -> moduleModel.getId().equals(t.getModuleId())).collect(Collectors.toList());
            model.setButton(JsonUtil.getJsonToList(buttonModels, PermissionVO.class));
            model.setColumn(JsonUtil.getJsonToList(columnModels, PermissionVO.class));
            model.setResource(JsonUtil.getJsonToList(resourceModels, PermissionVO.class));
            model.setForm(JsonUtil.getJsonToList(moduleFormModels, PermissionVO.class));
            if (moduleModel.getType() != 1) {
                models.add(model);
            }
        }
        //初始化接口权限
        if(securityProperties.isEnablePreAuth()) {
            initSecurityAuthorities(authorizeModel, userInfo, baseSystemInfo);
        }
        // 岗位
        List<UserRelationEntity> relationList = userRelationApi.getList(userEntity.getId(), PermissionConst.POSITION);
        List<String> positionIds = relationList.stream().map(UserRelationEntity::getObjectId).collect(Collectors.toList());
        List<PositionEntity> positionName = positionApi.getPositionName(positionIds, false).stream().filter(t -> t.getEnabledMark() != null && t.getEnabledMark() == 1).collect(Collectors.toList());
        List<UserPositionVO> positionIdVO = new ArrayList<>();
        positionName.forEach(t -> {
            if (!t.getOrganizeId().equals(userEntity.getOrganizeId())) {
                return;
            }
            UserPositionVO userPositionVO = new UserPositionVO();
            userPositionVO.setId(t.getId());
            userPositionVO.setName(t.getFullName());
            positionIdVO.add(userPositionVO);
        });
        infoVO.setPositionIds(positionIdVO);
        PositionEntity positionEntity = positionName.stream().filter(t -> t.getId().equals(userEntity.getPositionId())).findFirst().orElse(null);
        infoVO.setPositionId(positionEntity != null ? positionEntity.getId() : "");
        infoVO.setPositionName(positionEntity != null ? positionEntity.getFullName() : "");
        // 获取签名信息
        SignEntity signEntity = signService.getDefaultByUserId(userEntity.getId());
        infoVO.setSignImg(signEntity != null ? signEntity.getSignImg() : "");

        SystemInfo jsonToBean = JsonUtil.getJsonToBean(baseSystemInfo, SystemInfo.class);
        jsonToBean.setFutureDomain(futureOauthConfig.getFutureDomain());

        // 构建菜单树
        if (StringUtil.isNotEmpty(systemCode)) {
            systemList = systemList.stream().filter(t -> systemCode.equals(t.getEnCode())).collect(Collectors.toList());
            moduleList1 = moduleList1.stream().filter(t -> systemCodeEntity.getId().equals(t.getSystemId())).collect(Collectors.toList());
        }
        List<AllMenuSelectVO> menuSelectVOS = buildModule(systemList, moduleList1, type, userEntity, infoVO);
        List<AllMenuSelectVO> children = new ArrayList<>();
        AllMenuSelectVO allMenuSelectVO = null;
        if ("App".equals(type)) {
            allMenuSelectVO = menuSelectVOS.stream().filter(t -> userEntity.getAppSystemId().equals(t.getId())).findFirst().orElse(null);
        } else {
            if (StringUtil.isNotEmpty(systemCode)) {
                allMenuSelectVO = menuSelectVOS.stream().filter(t -> systemCode.equals(t.getEnCode())).findFirst().orElse(null);
            } else {
                allMenuSelectVO = menuSelectVOS.stream().filter(t -> userEntity.getSystemId().equals(t.getId())).findFirst().orElse(null);
            }
        }
        if (allMenuSelectVO != null && allMenuSelectVO.getChildren() != null) {
            children = allMenuSelectVO.getChildren();
            children.forEach(t -> t.setParentId("-1"));
        }
        if ("App".equals(type)) {
            infoVO.setAppSystemId(userEntity.getAppSystemId());
            userInfo.setAppSystemId(userEntity.getAppSystemId());
        } else {
            userInfo.setSystemId(userEntity.getSystemId());
            if (StringUtil.isNotEmpty(systemCode)) {
                infoVO.setSystemId(systemCodeEntity.getId());
            } else {
                infoVO.setSystemId(userEntity.getSystemId());
            }
        }
        // 设置系统模型
        List<UserSystemVO> jsonToList1 = new ArrayList<>();
        systemList.forEach(t -> {
            UserSystemVO systemVO = new UserSystemVO();
            systemVO.setId(t.getId());
            systemVO.setName(t.getFullName());
            systemVO.setIcon(t.getIcon());
            if ("App".equals(type) && userInfo.getAppSystemId().equals(t.getId())) {
                systemVO.setCurrentSystem(true);
            } else if ("Web".equals(type) && userInfo.getSystemId().equals(t.getId())) {
                systemVO.setCurrentSystem(true);
            }
            jsonToList1.add(systemVO);
        });
        infoVO.setSystemIds(jsonToList1);
        userInfo.setSystemIds(systemList.stream().map(SystemBaeModel::getId).collect(Collectors.toList()));
        SystemBaeModel systemBaeModel = systemList.stream().filter(t -> userInfo.getSystemId().equals(t.getId())).findFirst().orElse(null);
        if (systemBaeModel != null
//                && systemEntity.getIsMain() != null && systemEntity.getIsMain() != 1
        ) {
            jsonToBean.setNavigationIcon(systemBaeModel.getNavigationIcon());
            jsonToBean.setWorkLogoIcon(systemBaeModel.getWorkLogoIcon());
        }
        PcUserVO userVO = new PcUserVO(children, models, infoVO, jsonToBean);
        if (children.size() == 0 && ObjectUtil.equal(infoVO.getWorkflowEnabled(), 0)) {
            UserProvider.logout();
        }
//        userVO.setMenuList(menuList);
//        userVO.setPermissionList(models);
        userVO.getUserInfo().setHeadIcon(UploaderUtil.uploaderImg(userInfo.getUserIcon()));
        // 更新userInfo对象
        if (StringUtil.isNotEmpty(userInfo.getId())) {
            UserProvider.setLoginUser(userInfo);
            UserProvider.setLocalLoginUser(userInfo);
        }
        // 门户Web
        try{
            String defaultPortalId = portalApi.getCurrentDefault(PortalConst.WEB);
            infoVO.setPortalId(defaultPortalId);
        }catch (Exception e){
            infoVO.setPortalId("");
            e.printStackTrace();
        }
        // 门户App
        try{
            String defaultAppPortalId = portalApi.getCurrentDefault(PortalConst.APP);
            infoVO.setAppPortalId(defaultAppPortalId);
        }catch (Exception e){
            infoVO.setAppPortalId("");
            e.printStackTrace();
        }
        return userVO;
    }

    @Override
    public BaseSystemInfo getBaseSystemConfig(String tenantId) {
        if(tenantId != null){
            TenantDataSourceUtil.switchTenant(tenantId);
        }
        BaseSystemInfo info = sysconfigApi.getSysInfo(tenantId);
        return info;
    }

    private List<AllMenuSelectVO> buildModule(List<SystemBaeModel> systemList, List<ModuleModel> moduleList, String type, UserEntity entity, UserCommonInfoVO infoVO) {
        boolean enabledFow = false;
        if (configValueUtil.isMultiTenancy()) {
            TenantAuthorizeModel tenantAuthorizeModel = TenantDataSourceUtil.getCacheModuleAuthorize(UserProvider.getUser().getTenantId());
            List<String> cacheModuleAuthorize = tenantAuthorizeModel.getModuleIdList();
            if (cacheModuleAuthorize != null) {
                enabledFow = !cacheModuleAuthorize.contains("-999");
            }
        } else {
            enabledFow = true;
        }
        // 获取所有菜单树（区分Web、APP）
        moduleList = moduleList.stream().filter(t -> type.equals(t.getCategory())).sorted(Comparator.comparing(ModuleModel::getSortCode)).collect(Collectors.toList());
        String systemId = "Web".equals(type) ? entity.getSystemId() : entity.getAppSystemId();
        SystemBaeModel systemBaeModel = systemList.stream().filter(t -> t.getId().equals(systemId)).findFirst().orElse(null);
        if ("Web".equals(type)) {
            // 当前有协同，无需切换，直接放入协同菜单
            if (systemBaeModel != null && (enabledFow && Objects.equals(systemBaeModel.getWorkflowEnabled(), 1))) {
                List<ModuleEntity> listByEnCode = moduleApi.getListByEnCode(PlatformConst.MODULE_CODE);
                List<ModuleModel> jsonToList = JsonUtil.getJsonToList(listByEnCode, ModuleModel.class);
                jsonToList.forEach(t -> {
                    if ("-1".equals(t.getParentId())) {
                        t.setSortCode(-999L);
                        t.setParentId(entity.getSystemId());
                    }
                });
                moduleList.addAll(jsonToList);
                infoVO.setWorkflowEnabled(1);
            } else if (systemBaeModel == null || !enabledFow || (Objects.equals(systemBaeModel.getWorkflowEnabled(), 0) && moduleList.stream().noneMatch(t -> t.getSystemId().equals(entity.getSystemId())))) {
                // 当前无协同，需切换，优先找开启协同的
                String currentSystemId = "";
                if (moduleList.stream().filter(t -> t.getSystemId().equals(entity.getSystemId())).count() == 0) {
                    for (SystemBaeModel baeModel : systemList) {
                        if (Objects.equals(baeModel.getWorkflowEnabled(), 1)) {
                            currentSystemId = baeModel.getId();
                            break;
                        }
                    }
                    if (StringUtil.isNotEmpty(currentSystemId)) {
                        List<ModuleEntity> listByEnCode = moduleApi.getListByEnCode(PlatformConst.MODULE_CODE);
                        List<ModuleModel> jsonToList = JsonUtil.getJsonToList(listByEnCode, ModuleModel.class);
                        String finalCurrentSystemId = currentSystemId;
                        jsonToList.forEach(t -> {
                            if ("-1".equals(t.getParentId())) {
                                t.setSortCode(-999L);
                                t.setParentId(finalCurrentSystemId);
                            }
                        });
                        moduleList.addAll(jsonToList);
                        infoVO.setWorkflowEnabled(1);
                    }
                    // 都未开启协同，找有菜单的
                    if (infoVO.getWorkflowEnabled() == 0 && moduleList.size() > 0) {
                        currentSystemId = moduleList.get(0).getSystemId();
                    }
                    entity.setSystemId(currentSystemId);
                }
            }
        } else {
            if (systemBaeModel != null && enabledFow && Objects.equals(systemBaeModel.getWorkflowEnabled(), 1)) {
                infoVO.setWorkflowEnabled(1);
                entity.setAppSystemId(systemBaeModel.getId());
            } else if (systemBaeModel == null || !enabledFow || (Objects.equals(systemBaeModel.getWorkflowEnabled(), 0) && moduleList.stream().noneMatch(t -> t.getSystemId().equals(entity.getAppSystemId())))) {
                // 当前无协同，需切换，优先找开启协同的
                String currentSystemId = "";
                if (moduleList.stream().filter(t -> t.getSystemId().equals(entity.getAppSystemId())).count() == 0) {
                    for (SystemBaeModel baeModel : systemList) {
                        if (Objects.equals(baeModel.getWorkflowEnabled(), 1)) {
                            currentSystemId = baeModel.getId();
                            break;
                        }
                    }
                    if (StringUtil.isNotEmpty(currentSystemId)) {
                        infoVO.setWorkflowEnabled(1);
                    }
                    // 都未开启协同，找有菜单的
                    if (infoVO.getWorkflowEnabled() == 0 && moduleList.size() > 0) {
                        currentSystemId = moduleList.get(0).getSystemId();
                    }
                    entity.setAppSystemId(currentSystemId);
                }
            }
        }
        moduleList = moduleList.stream().sorted(Comparator.comparing(ModuleModel::getSortCode)).collect(Collectors.toList());
        List<AllUserMenuModel> list = JsonUtil.getJsonToList(moduleList, AllUserMenuModel.class);
        list.forEach(t -> {
            if ("-1".equals(t.getParentId())) {
                t.setParentId(t.getSystemId());
            }
        });
        List<AllUserMenuModel> jsonToList = JsonUtil.getJsonToList(systemList, AllUserMenuModel.class);
        jsonToList.forEach(t -> {
            t.setType(0);
            t.setParentId("-1");
        });
        list.addAll(jsonToList);
        List<SumTree<AllUserMenuModel>> menuList = TreeDotUtils.convertListToTreeDotFilter(list);
        List<AllMenuSelectVO> menuvo = JsonUtil.getJsonToList(menuList, AllMenuSelectVO.class);
        return menuvo;
    }

    /**
     * 初始化接口鉴权用的账号权限
     * 本接口插入权限缓存， SaInterfaceImpl中框架鉴权时动态调用获取权限列表
     * @param authorizeModel
     * @param userInfo
     */
    private void initSecurityAuthorities(AuthorizeVO authorizeModel, UserInfo userInfo, BaseSystemInfo systemInfo){
        //接口权限
        Set<String> authorityList = new HashSet<>();
        Map<String, ModuleModel> moduleModelMap = authorizeModel.getModuleList().stream().filter(m->{
            //添加菜单权限
            authorityList.add(m.getEnCode());
            return true;
        }).collect(Collectors.toMap(ModuleModel::getId, m->m));
        for (ModuleModel moduleModel : authorizeModel.getModuleList()) {
            String permissionKey = moduleModel.getEnCode();
            authorityList.add(permissionKey);
            //功能菜单、大屏
            if(moduleModel.getType() == 3 || moduleModel.getType() == 6){
                JSONObject propertyJSON = JSONObject.parseObject(Optional.of(moduleModel.getPropertyJson()).orElse("{}"));
                //{"iconBackgroundColor":"","isTree":0,"moduleId":"395851986114733317"}
                String moduleId = propertyJSON.getString("moduleId");
                if(!StringUtil.isEmpty(moduleId)){
                    authorityList.add(moduleId);
                }
            }
        }

        //按钮权限 菜单编码::按钮编码
        authorizeModel.getButtonList().forEach(t -> {
            ModuleModel m = moduleModelMap.get(t.getModuleId());
            if(m != null){
                authorityList.add(m.getEnCode() + "::" + t.getEnCode());
            }
        });
        //列表权限 菜单编码::列表编码
        authorizeModel.getColumnList().forEach(t -> {
            ModuleModel m = moduleModelMap.get(t.getModuleId());
            if(m != null){
                authorityList.add(m.getEnCode() + "::" + t.getEnCode());
            }
        });
        //表单权限 菜单编码::表单编码
        authorizeModel.getFormsList().forEach(t -> {
            ModuleModel m = moduleModelMap.get(t.getModuleId());
            if(m != null){
                authorityList.add(m.getEnCode() + "::" + t.getEnCode());
            }
        });

        //管理员都是用同一个缓存, 普通账号使用账号名,
        //权限列表：authorize_:租户_authorize_authorize_(admin|账号)
        //角色列表：authorize_:租户_authorize_role_(admin|账号)
        String account = userInfo.getIsAdministrator()? ADMIN_KEY :userInfo.getUserId();
        PermissionInterfaceImpl.setAuthorityList(account, authorityList, systemInfo);
        if (userInfo.getRoleIds() != null && !userInfo.getRoleIds().isEmpty() || userInfo.getIsAdministrator()) {
            List<RoleEntity> roles;
            if(userInfo.getIsAdministrator()){
                roles = roleApi.getListAll();
            }else{
                roles = roleApi.getListByIds(userInfo.getRoleIds());
            }
            Set<String> roleAuthorityList = roles.stream().filter(r->r.getEnabledMark().equals(1)).map(r -> "ROLE_" + r.getEnCode()).collect(Collectors.toSet());
            PermissionInterfaceImpl.setRoleList(account, roleAuthorityList, systemInfo);
        }
    }

    /**
     * 获取下属机构
     *
     * @param data
     * @param organizeId
     * @param isAdmin
     * @return
     */
    private String[] getSubOrganizeIds(List<OrganizeEntity> data, String organizeId, boolean isAdmin) {
        if (!isAdmin) {
            data = JsonUtil.getJsonToList(ListToTreeUtil.treeWhere(organizeId, data), OrganizeEntity.class);
        }
        return data.stream().map(SuperBaseEntity.SuperIBaseEntity::getId).toArray(String[]::new);
    }

    /**
     * 赋值
     *  @param userInfo
     * @param userId
     * @param isAdmin
     * @param systemId
     */
    private void userInfo(UserInfo userInfo, String userId, boolean isAdmin, UserEntity userEntity, String systemId) {
        // 得到用户和组织的关系
        List<UserRelationEntity> data = userRelationApi.getList(userId, PermissionConst.ORGANIZE);
        // 组织id
        String organizeId = userEntity.getOrganizeId();
        String departmentId = "";
        List<String> roleId = new ArrayList<>();
        // 判断当前组织是否有权限
        if(organizeRelationApi.checkBasePermission(userEntity.getId(), userEntity.getOrganizeId(), systemId).size() == 0) {
            if (data.size() > 0) {
                // 得到组织id
                organizeId = organizeRelationApi.autoGetMajorOrganizeId(new AutoGetMajorOrgIdModel(userId, data.stream().map(UserRelationEntity::getObjectId).collect(Collectors.toList()), userEntity.getOrganizeId(), systemId));
            }
        } else {
            // 如果有权限
            organizeId = userEntity.getOrganizeId();
//            if (isAdmin) {
//                roleId = data.stream().map(t -> t.getObjectId()).collect(Collectors.toList());
//            }
        }
        // 获取用户的角色
        List<UserRelationEntity> listByObjectId = userRelationApi.getList(userInfo.getUserId(), PermissionConst.ROLE);
        // 判断哪个角色是当前组织下的
        List<String> collect = listByObjectId.stream().filter(t -> StringUtil.isNotEmpty(t.getObjectId())).map(UserRelationEntity::getObjectId).collect(Collectors.toList());
        // 如果有全局的角色则先赋值给权限集合
        for (String roleIds : collect) {
            // 得到角色
            RoleEntity info = roleApi.getInfoById(roleIds);
            if (info != null && "1".equals(String.valueOf(info.getGlobalMark()))) {
                roleId.add(info.getId());
                continue;
            }
            // 判断哪些角色是当前组织的
            Boolean exist = organizeRelationApi.existByRoleIdAndOrgId(roleIds, organizeId);
            if (exist) {
                roleId.add(roleIds);
            }
        }
        // 获取分组
        List<UserRelationEntity> groupRelationByUserId = userRelationApi.getList(userInfo.getUserId(), PermissionConst.GROUP);
        List<String> groupIds = groupRelationByUserId.stream().map(UserRelationEntity::getObjectId).collect(Collectors.toList());
        List<GroupEntity> groupName = groupApi.getGroupName(ImmutableMap.of("ids", groupIds, "filterEnableMark", "true"));
        userInfo.setGroupIds(groupName.stream().map(GroupEntity::getId).collect(Collectors.toList()));
        userInfo.setGroupNames(groupName.stream().map(GroupEntity::getFullName).collect(Collectors.toList()));
        // 赋值岗位
//        List<String> positionList = userRelationApi.getList(userInfo.getUserId(), PermissionConst.POSITION)
//                .stream().map(UserRelationEntity::getObjectId).collect(Collectors.toList());
//        Set<String> id = new LinkedHashSet<>();
//        String[] position = StringUtil.isNotEmpty(userEntity.getPositionId()) ? userEntity.getPositionId().split(",") : new String[]{};
//        List<String> positions = positionList.stream().filter(t -> Arrays.asList(position).contains(t)).collect(Collectors.toList());
//        id.addAll(positions);
//        id.addAll(positionList);
//        String[] positionId = id.toArray(new String[id.size()]);
        userInfo.setOrganizeId(organizeId);
        userInfo.setDepartmentId(departmentId);
        userInfo.setRoleIds(roleId);
        userInfo.setPositionIds(new String[]{userEntity.getPositionId()});
        // 处理userInfo
        userInfo.setSystemId(userEntity.getSystemId());
        userInfo.setAppSystemId(userEntity.getAppSystemId());
        // 修改用户信息
        userEntity.setOrganizeId(organizeId);
        userEntity.setPositionId(organizeRelationApi.autoGetMajorPositionId(userId, organizeId, userEntity.getPositionId()));
        userApi.updateById(new UserUpdateModel(userEntity, userInfo.getTenantId()));
    }


    /**
     * 登录信息
     *
     * @param userInfo   回话信息
     * @param systemInfo 系统信息
     * @return
     */
    private Map<String, Object> genUserInfo(UserInfo userInfo, BaseSystemInfo systemInfo) {
        Map<String, Object> dictionary = new HashMap<>(16);
        dictionary.put("userId", userInfo.getUserId());
        dictionary.put("userAccount", userInfo.getUserAccount());
        dictionary.put("userName", userInfo.getUserName());
        dictionary.put("icon", userInfo.getUserIcon());
        dictionary.put("portalId", userInfo.getPortalId());
        dictionary.put("gender", userInfo.getUserGender());
        dictionary.put("organizeId", userInfo.getOrganizeId());
        dictionary.put("prevLogin", systemInfo.getLastLoginTimeSwitch() == 1 ? 1 : 0);
        dictionary.put("prevLoginTime", userInfo.getPrevLoginTime());
        dictionary.put("prevLoginIPAddress", userInfo.getPrevLoginIpAddress());
        dictionary.put("prevLoginIPAddressName", userInfo.getPrevLoginIpAddressName());
        dictionary.put("serviceDirectory", configValueUtil.getServiceDirectoryPath());
        dictionary.put("webDirectory", configValueUtil.getCodeAreasName());
        dictionary.put("isAdministrator", userInfo.getIsAdministrator());
        return dictionary;
    }

}
