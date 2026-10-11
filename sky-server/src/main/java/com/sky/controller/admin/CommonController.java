package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Slf4j
@Api(tags = "通用的控制器")
public class CommonController {
//    因为前端传递过来的类型是一个文件类型，可以直接用一个文件对象来进行接收body中的文件,其中这里的字段要和前端传递过来的字段保持一致，这样可以直接进行匹配到
    @Autowired
    private AliOssUtil aliOssUtil;
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file)  {
        log.info("文件上传:{}",file);
//        为了防止文件传上去会重命名的情况，所以在这里可以使用uuid来进行重命名
        try {
            String originalFilename = file.getOriginalFilename();
//        截取原始文件名后缀,从最后一个开始截取获取文件的扩展名
            String extension=originalFilename.substring(originalFilename.lastIndexOf("."));
            //构建新的文件名
            String objectName= UUID.randomUUID().toString()+extension;
            String filePath=aliOssUtil.upload(file.getBytes(), objectName);
            return Result.success(filePath);
        }catch (Exception e){
            log.error("文件上传失败",e);
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
