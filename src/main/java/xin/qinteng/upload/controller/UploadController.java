package xin.qinteng.upload.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import xin.qinteng.upload.client.FastDFSClientWrapper;

import java.io.IOException;

@RestController
public class UploadController {
    private final Logger logger = LoggerFactory.getLogger(UploadController.class);
    private final FastDFSClientWrapper fastDFSClientWrapper;

    @Autowired
    public UploadController(FastDFSClientWrapper fastDFSClientWrapper) {
        this.fastDFSClientWrapper = fastDFSClientWrapper;
    }

    @RequestMapping(value = "uploadFile", method = RequestMethod.POST)
    public String uploadFile(MultipartFile file) {
        String FullPath = null;
        try {
            FullPath = fastDFSClientWrapper.uploadFile(file);
        } catch (IOException e) {
            logger.error("upload error:", e);
        }
        return FullPath;
    }

}
