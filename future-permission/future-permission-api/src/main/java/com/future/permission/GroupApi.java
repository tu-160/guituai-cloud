package com.future.permission;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.future.feign.utils.FeignName;
import com.future.permission.entity.GroupEntity;
import com.future.permission.entity.UserEntity;
import com.future.permission.fallback.GroupApiFallback;

import java.util.List;
import java.util.Map;

/**
 * 获取分组信息Api
 *
 * @author Future Platform Group
 * @version v4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2022/8/19
 */
@FeignClient(name = FeignName.PERMISSION_SERVER_NAME, fallback = GroupApiFallback.class, path = "/Group")
public interface GroupApi {

	/**
	 * 通过id获取分组信息
	 *
	 * @param groupId
	 * @return
	 */
	@GetMapping("/getInfoById/{groupId}")
	GroupEntity getInfoById(@PathVariable("groupId") String groupId);

	@GetMapping("/getGroupMap")
	Map<String, Object> getGroupMap(@RequestParam("type") String type);

	@PostMapping("/getGroupName")
	List<GroupEntity> getGroupName(@RequestBody Map<String, Object> map);
}
