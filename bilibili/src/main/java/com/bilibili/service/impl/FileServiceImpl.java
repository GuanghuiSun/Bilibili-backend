package com.bilibili.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bilibili.exception.BusinessException;
import com.bilibili.model.domain.File;
import com.bilibili.service.FileService;
import com.bilibili.mapper.FileMapper;
import com.bilibili.utils.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static com.bilibili.base.ErrorCode.GET_SERVICE_ERROR;
import static com.bilibili.constant.MessageConstant.*;

/**
 * @author sgh
 * @description 针对表【t_file(上传文件相关信息表)】的数据库操作Service实现
 * @createDate 2022-08-06 15:27:46
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File>
        implements FileService {

    @Override
    public void addFile(File file) {
        this.save(file);
    }

    @Override
    public String getFileByMd5(String fileMd5) {
        LambdaQueryWrapper<File> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(File::getMd5, fileMd5);
        File file = this.getOne(wrapper);
        if (file == null) {
            throw new BusinessException(GET_SERVICE_ERROR, FILE_NOT_EXIST_ERROR);
        }
        return file.getUrl();
    }

    @Override
    public String getFileMd5(MultipartFile multipartFile) {
        String fileMd5 = MD5Util.getFileMd5(multipartFile);
        if (StringUtils.isBlank(fileMd5)) {
            throw new BusinessException(GET_MESSAGE_ERROR_CODE, FILE_MD5_ERROR);
        }
        return fileMd5;
    }
}




