package com.future.permission.model.position;

import com.alibaba.fastjson.annotation.JSONField;
import com.future.common.util.treeutil.SumTree;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021/3/12 15:31
 */
@Data
public class PosOrgModel extends SumTree {

   @Schema(description ="名称")
   private String fullName;
   @Schema(description ="状态")
   private Integer enabledMark;
   @JSONField(name = "category")
   private String type;
   @Schema(description ="图标")
   private String icon;
   @Schema(description ="排序")
   private String sortCode;
   @Schema(description ="创建时间")
   private Date creatorTime;


   private String organize;
   @Schema(description ="组织id树")
   private List<String> organizeIds;
}
