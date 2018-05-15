package com.wraith.money.web.controller;

import com.wraith.money.web.service.DataUploadService;
import org.springframework.batch.core.BatchStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;

/**
 * This controller manages the data upload request functionality.
 * <p/>
 * User: rowan.massey
 * Date: 26/08/2014
 */
@Controller
public class DataUploadController {

    @Inject
    private DataUploadService dataUploadService;

    @RequestMapping(value = "/api/$service/dataUpload", method = RequestMethod.POST)
    @ResponseBody
    public BatchStatus performDataUpload(@RequestParam("file") MultipartFile file, @RequestParam("uploadType") String uploadType) {
        return dataUploadService.performUploadData(file, uploadType).getStatus();
    }
}
