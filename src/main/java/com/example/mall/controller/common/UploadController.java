package com.example.mall.controller.common;

import com.example.mall.common.Constants;
import com.example.mall.util.MallUtils;
import com.example.mall.util.Result;
import com.example.mall.util.ResultGenerator;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 *
 */
@Controller
@RequestMapping("/admin")
public class UploadController {

    @RequestMapping(method = RequestMethod.POST, value = {"/upload/file"})
    @ResponseBody
    public Result upload(HttpServletRequest request,
                         @RequestParam("file") MultipartFile file) throws URISyntaxException {
        try {
            Result resultSuccess = ResultGenerator.genSuccessResult();
            resultSuccess.setData(this.upload(file, request));
            return resultSuccess;
        } catch (IOException e) {
            e.printStackTrace();
            return ResultGenerator.genFailResult("文件上传失败");
        }
    }


    /**
     * 上传文件
     *
     * @param file file
     * @return Map
     */
    @RequestMapping(value = "/editor/upload", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public Map<String, Object> uploadFile(@RequestParam("file") MultipartFile file,
                                          HttpServletRequest request) throws IOException, URISyntaxException {
        Map<String, Object> map = new HashMap<>(1);
        String path = this.upload(file, request);
        map.put("location", path);
        return map;
    }

    /**
     * 文件上传
     *
     * @param file
     * @param request
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    private String upload(MultipartFile file, HttpServletRequest request) throws IOException, URISyntaxException {
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //生成文件名称通用方法
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Random r = new Random();
        StringBuilder tempName = new StringBuilder();
        tempName.append(sdf.format(new Date())).append(r.nextInt(100)).append(suffixName);
        String newFileName = tempName.toString();
        File fileDirectory = new File(Constants.FILE_UPLOAD_DIC);
        //创建文件
        File destFile = new File(Constants.FILE_UPLOAD_DIC + newFileName);
        if (!fileDirectory.exists()) {
            if (!fileDirectory.mkdir()) {
                throw new IOException("文件夹创建失败,路径为：" + fileDirectory);
            }
        }
        file.transferTo(destFile);
        return MallUtils.getHost(new URI(request.getRequestURL() + "")) + "/upload/" + newFileName;
    }

}
