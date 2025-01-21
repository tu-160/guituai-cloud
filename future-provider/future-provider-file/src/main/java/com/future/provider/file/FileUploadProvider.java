package com.future.provider.file;

import java.util.List;

import com.future.common.model.FileListVO;
import com.future.module.file.model.VisualDataImgModel;
import com.future.module.file.model.upload.DownFileModel;
import com.future.module.file.model.upload.UploadFileModel;

import cn.xuyanwu.spring.file.storage.FileInfo;

/**
 * 文件上传API
 *
 * @author Future Platform Group
 * @version V4.0.0
 * @copyright 直方信息科技有限公司
 * @date 2021-07-23
 */
public interface FileUploadProvider {

    /**
     * 文件上传
     *
     * @param model
     * @return
     */
    FileInfo uploadFiles(UploadFileModel model);

    /**
     * 删除一个对象
     *
     * @param folderName
     * @param fileName
     */
    void removeFile(String folderName, String fileName);

    /**
     * 下载文件
     *
     * @param downFileModel   下载文件模型
     * @return
     */
    boolean downFile(DownFileModel downFileModel);

    /**
     * 直接在页面上显示图片
     *
     * @param downFileModel   下载文件模型
     * @return
     */
    boolean writeImage(DownFileModel downFileModel);

    /**
     * 压缩文件夹后上传压缩包
     *
     * @param model   文件上传模型
     * @return
     */
    void putFolder(UploadFileModel model);

    /**
     * 获取存储桶下文件夹中的内容
     *FileListVO
     * @param folderName    存储桶名
     * @return
     */
    List<FileListVO> getFileList(String folderName);

    /**
     * 获取存储桶下文件夹中的内容
     *
     * @param
     * @return
     */
    List<FileListVO> getVisualList(String folderName);

    /**
     * 通过流下载文件
     *
     * @param model
     */
    void downToLocal(UploadFileModel model);

    /**
     * 下载文件 返回流
     *
     * @param fileName   文件名
     * @param folderName 存储桶名
     * @return
     */
    byte[] getInputStream(String fileName, String folderName);

    /**
     * 下载代码
     *
     * @param bytes
     * @param fileName
     * @param name
     */
    void downloadFile(byte[] bytes, String fileName, String name);

    /**
     * 获取默认模板路径
     *

     */
    String getDefaultPlatform();
}
