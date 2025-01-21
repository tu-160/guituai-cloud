package com.future.module.system.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.Operation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.future.base.controller.SuperController;
import com.future.common.base.ActionResult;
import com.future.common.base.vo.ListVO;
import com.future.common.util.*;
import com.future.module.message.MessageTemplateConfigApi;
import com.future.module.message.entity.SendMessageConfigEntity;
import com.future.module.system.DictionaryDataApi;
import com.future.module.system.entity.DictionaryDataEntity;
import com.future.module.system.entity.ScheduleNewEntity;
import com.future.module.system.entity.ScheduleNewUserEntity;
import com.future.module.system.model.schedule.*;
import com.future.module.system.service.ScheduleNewService;
import com.future.module.system.service.ScheduleNewUserService;
import com.future.permission.UserApi;
import com.future.permission.entity.UserEntity;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 日程
 *
 * @author Future Platform Group
 * @copyright 直方信息科技有限公司
 */
@Tag(name = "日程", description = "Schedule")
@RestController
@RequestMapping("/Schedule")
public class ScheduleNewController extends SuperController<ScheduleNewService, ScheduleNewEntity> {


    @Autowired
    private UserApi userService;
    @Autowired
    private MessageTemplateConfigApi messageTemplateConfigService;
    @Autowired
    private DictionaryDataApi dictionaryDataService;
    @Autowired
    private ScheduleNewService scheduleNewService;
    @Autowired
    private ScheduleNewUserService scheduleNewUserService;

    /**
     * 获取日程安排列表
     *
     * @param scheduleNewTime 分页模型
     * @return
     */
    @Operation(summary = "获取日程安排列表")
    @GetMapping
    public ActionResult<ListVO<ScheduleNewListVO>> list(ScheduleNewTime scheduleNewTime) {
        List<ScheduleNewEntity> list = scheduleNewService.getList(scheduleNewTime);
        Date start = DateUtil.stringToDates(scheduleNewTime.getStartTime());
        Date end = DateUtil.stringToDates(scheduleNewTime.getEndTime());
        List<Date> dataAll = DateUtil.getAllDays(start, end);
        List<ScheduleNewEntity> result = new ArrayList<>();
        if (list.size() > 0) {
            for (Date date : dataAll) {
                for (ScheduleNewEntity entity : list) {
                    Date startDay = DateUtil.stringToDates(DateUtil.daFormat(entity.getStartDay()));
                    Date endDay = DateUtil.stringToDates(DateUtil.daFormat(entity.getEndDay()));
                    if(DateUtil.isEffectiveDate(date,startDay,endDay)){
                        result.add(entity);
                    }
                }
            }
        }
        for (ScheduleNewEntity entity : result) {
            if (entity.getAllDay() == 1) {
                entity.setEndDay(DateUtil.dateAddSeconds(entity.getEndDay(), 1));
            }
        }
        List<ScheduleNewListVO> vo = JsonUtil.getJsonToList(result, ScheduleNewListVO.class);
        ListVO listVO = new ListVO();
        listVO.setList(vo);
        return ActionResult.success(listVO);
    }

    /**
     * 获取日程安排列表
     *
     * @param scheduleNewTime 分页模型
     * @return
     */
    @Operation(summary = "获取日程安排列表")
    @GetMapping("/AppList")
    public ActionResult<ScheduleNewAppListVO> selectList(ScheduleNewTime scheduleNewTime) {
        Map<String, Object> signMap = new HashMap<>(16);
        List<ScheduleNewEntity> list = scheduleNewService.getList(scheduleNewTime);
        Date start = DateUtil.stringToDates(scheduleNewTime.getStartTime());
        Date end = DateUtil.stringToDates(scheduleNewTime.getEndTime());
        List<Date> dateList = new ArrayList() {{
            add(start);
            add(end);
        }};
        if(StringUtils.isNotEmpty(scheduleNewTime.getDateTime())){
            dateList.add(DateUtil.strToDate(scheduleNewTime.getDateTime()));
        }
        Date minDate = dateList.stream().min(Date::compareTo).get();
        Date maxDate = dateList.stream().max(Date::compareTo).get();
        List<Date> dataAll = DateUtil.getAllDays(minDate, maxDate);
        ScheduleNewAppListVO vo = new ScheduleNewAppListVO();
        String pattern = "yyyyMMdd";
        String dateTime = StringUtils.isEmpty(scheduleNewTime.getDateTime()) ? DateUtil.dateNow(pattern) : scheduleNewTime.getDateTime().replaceAll("-", "");
        List<ScheduleNewEntity> todayList = new ArrayList<>();
        for (Date date : dataAll) {
            String time = DateUtil.dateToString(date, pattern);
            List<ScheduleNewEntity> result = new ArrayList<>();
            for (ScheduleNewEntity entity : list) {
                Date startDay = DateUtil.stringToDates(DateUtil.daFormat(entity.getStartDay()));
                Date endDay = DateUtil.stringToDates(DateUtil.daFormat(entity.getEndDay()));
                if(DateUtil.isEffectiveDate(date,startDay,endDay)){
                    result.add(entity);
                }
            }
            signMap.put(time, result.size());
            if(time.equals(dateTime)){
                todayList.addAll(result);
            }
        }
        vo.setSignList(signMap);
        vo.setTodayList(JsonUtil.getJsonToList(todayList, ScheduleNewListVO.class));
        return ActionResult.success(vo);
    }

    /**
     * 信息
     *
     * @param id 主键
     * @return
     */
    @Operation(summary = "获取日程安排信息")
    @GetMapping("/{id}")
    @Parameters({
            @Parameter(name = "id", description = "主键", required = true),
    })
    public ActionResult<ScheduleNewInfoVO> info(@PathVariable("id") String id) {
        ScheduleNewEntity entity = scheduleNewService.getInfo(id);
        ScheduleNewInfoVO vo = JsonUtil.getJsonToBean(entity, ScheduleNewInfoVO.class);
        if (vo != null) {
            SendMessageConfigEntity config = messageTemplateConfigService.getSendMessageConfig(vo.getSend());
            vo.setSendName(config!=null?config.getFullName():"");
            List<String> toUserIds = scheduleNewUserService.getList(entity.getId(),2).stream().map(ScheduleNewUserEntity::getToUserId).collect(Collectors.toList());
            vo.setToUserIds(toUserIds);
            return ActionResult.success(vo);
        }
        return ActionResult.fail("数据不存在");
    }

    /**
     * 信息
     *
     * @param detailModel 查询模型
     * @return
     */
    @Operation(summary = "获取日程安排信息")
    @GetMapping("/detail")
    public ActionResult<ScheduleNewDetailInfoVO> detail(ScheduleDetailModel detailModel) {
        List<ScheduleNewEntity> groupList = scheduleNewService.getGroupList(detailModel);
        ScheduleNewEntity entity = groupList.size() > 0 ? groupList.get(0) : null;
        boolean isVO = entity != null;
        if (isVO) {
            ScheduleNewDetailInfoVO vo = JsonUtil.getJsonToBean(entity, ScheduleNewDetailInfoVO.class);
            DictionaryDataEntity info = dictionaryDataService.getInfo(entity.getCategory());
            vo.setCategory(info != null ? info.getFullName() : "");
            vo.setUrgent("1".equals(vo.getUrgent()) ? "普通" : "2".equals(vo.getUrgent()) ? "重要" : "紧急");
            UserEntity infoById = userService.getInfoById(vo.getCreatorUserId());
            vo.setCreatorUserId(infoById != null ? infoById.getRealName() + "/" + infoById.getAccount() : "");
            List<String> toUserIds = scheduleNewUserService.getList(entity.getId(),2).stream().map(ScheduleNewUserEntity::getToUserId).collect(Collectors.toList());
            List<UserEntity> userName = userService.getUserName(toUserIds);
            StringJoiner joiner = new StringJoiner(",");
            for (UserEntity userEntity : userName) {
                joiner.add(userEntity.getRealName() + "/" + userEntity.getAccount());
            }
            vo.setToUserIds(joiner.toString());
            return ActionResult.success(vo);
        }
        return ActionResult.fail("该日程已被删除");
    }

    /**
     * 新建
     *
     * @param scheduleCrForm 日程模型
     * @return
     */
    @Operation(summary = "新建日程安排")
    @PostMapping
    @Parameters({
            @Parameter(name = "scheduleCrForm", description = "日程模型",required = true),
    })
    public ActionResult create(@RequestBody @Valid ScheduleNewCrForm scheduleCrForm) {
        ScheduleNewEntity entity = JsonUtil.getJsonToBean(scheduleCrForm, ScheduleNewEntity.class);
        scheduleNewService.create(entity, scheduleCrForm.getToUserIds(), RandomUtil.uuId(),"1",new ArrayList<>());
        return ActionResult.success("新建成功");
    }

    /**
     * 更新
     *
     * @param id             主键
     * @param scheduleUpForm 日程模型
     * @param type           1.此日程 2.此日程及后续 3.所有日程
     * @return
     */
    @Operation(summary = "更新日程安排")
    @PutMapping("/{id}/{type}")
    @Parameters({
            @Parameter(name = "id", description = "主键", required = true),
            @Parameter(name = "scheduleUpForm", description = "日程模型", required = true),
            @Parameter(name = "type", description = "类型", required = true),
    })
    public ActionResult update(@PathVariable("id") String id, @RequestBody @Valid ScheduleNewUpForm scheduleUpForm, @PathVariable("type") String type) {
        if("1".equals(type)){
            scheduleUpForm.setRepeatTime(null);
            scheduleUpForm.setRepetition(1);
        }
        ScheduleNewEntity entity = JsonUtil.getJsonToBean(scheduleUpForm, ScheduleNewEntity.class);
        boolean flag = scheduleNewService.update(id, entity, scheduleUpForm.getToUserIds(), type);
        if (flag == false) {
            return ActionResult.fail("更新失败，数据不存在");
        }
        return ActionResult.success("更新成功");
    }

    /**
     * 删除
     *
     * @param id   主键
     * @param type           1.此日程 2.此日程及后续 3.所有日程
     * @return
     */
    @Operation(summary = "删除日程安排")
    @DeleteMapping("/{id}/{type}")
    @Parameters({
            @Parameter(name = "id", description = "主键", required = true),
            @Parameter(name = "type", description = "类型", required = true),
    })
    public ActionResult delete(@PathVariable("id") String id, @PathVariable("type") String type) {
        ScheduleNewEntity entity = scheduleNewService.getInfo(id);
        if (entity != null) {
            scheduleNewService.delete(entity, type);
            return ActionResult.success("删除成功");
        }
        return ActionResult.fail("删除失败，数据不存在");
    }

}
