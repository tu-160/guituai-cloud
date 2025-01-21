package com.future.permission.fallback;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import com.future.permission.GroupApi;
import com.future.permission.entity.GroupEntity;

import java.util.*;

@Component
public class GroupApiFallback implements GroupApi {
	@Override
	public GroupEntity getInfoById(String groupId) {
		return new GroupEntity();
	}

	@Override
	public Map<String, Object> getGroupMap(String type) {
		return new HashMap<>();
	}

	@Override
	public List<GroupEntity> getGroupName(Map<String, Object> map) {
		return Collections.EMPTY_LIST;
	}
}
