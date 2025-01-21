//package com.future.module.system.controller;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.future.common.base.ActionResult;
//import com.future.common.base.Pagination;
//import com.future.common.base.UserInfo;
//import com.future.common.base.vo.PaginationVO;
//import com.future.common.constant.FileTypeConstant;
//import com.future.common.exception.DataException;
//import com.future.common.util.FileUtil;
//import com.future.common.util.JsonUtil;
//import com.future.common.util.UserProvider;
//import com.future.file.util.UploaderUtil;
//import com.future.module.file.FileApi;
//import com.future.module.system.entity.DbBackupEntity;
//import com.future.module.system.model.dbbackup.DbBackupListVO;
//import com.future.module.system.service.DbBackupService;
//
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//
///**
// * 数据备份
// *
// * @author Future Platform Group
// * @version V4.0.0
// * @copyright 直方信息科技有限公司
// * @date 2019年9月27日 上午9:18
// */
//@Tag(name = "数据备份", description = "DataBackup")
//@RestController
//@RequestMapping("/DataBackup")
//public class DataBackupController {
//
//    @Autowired
//    private DbBackupService dbBackupService;
//    @Autowired
//    private UserProvider userProvider;
//    @Autowired
//    private FileApi fileApi;
//
//    /**
//     * 列表
//     *
//     * @param pagination
//     * @return
//     */
//    @Operation(summary = "获取数据备份列表(带分页)")
//    @GetMapping
//    public ActionResult list(Pagination pagination) {
//        UserInfo userInfo = userProvider.get();
//        List<DbBackupEntity> list = dbBackupService.getList(pagination);
//
//        PaginationVO paginationVO = JsonUtil.getJsonToBean(pagination, PaginationVO.class);
//        List<DbBackupListVO> listVos = JsonUtil.getJsonToList(list, DbBackupListVO.class);
//        for (DbBackupListVO dbList : listVos) {
//            String filePath = fileApi.getPath(FileTypeConstant.DATABACKUP) + dbList.getFileName();
//            if (FileUtil.fileIsFile(filePath)) {
//                dbList.setFileUrl(UploaderUtil.uploaderFile(dbList.getFileName() + "#dataBackup"));
//            }
//        }
//        return ActionResult.page(listVos, paginationVO);
//    }
//
//    /**
//     * 创建备份
//     *
//     * @return
//     */
//    @Operation(summary = "添加数据备份")
//    @PostMapping
//    public ActionResult create() {
//        boolean flag = dbBackupService.dbBackup();
//        if (flag) {
//            return ActionResult.success("备份成功");
//        } else {
//            return ActionResult.fail("备份失败");
//        }
//    }
//
//    /**
//     * 删除
//     *
//     * @param id 主键值
//     * @return
//     */
//    @Operation(summary = "删除数据备份")
//    @DeleteMapping("/{id}")
//    public ActionResult delete(@PathVariable("id") String id) throws DataException {
//        DbBackupEntity entity = dbBackupService.getInfo(id);
//        if (entity != null) {
//            dbBackupService.delete(entity);
//            return ActionResult.success("删除成功");
//        }
//        return ActionResult.fail("删除失败，数据不存在");
//    }
//}
