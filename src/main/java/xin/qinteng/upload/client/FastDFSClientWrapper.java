package xin.qinteng.upload.client;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.exception.FdfsUnsupportStorePathException;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * FastDFS文件上传下载包装类
 *
 * @author qinteng
 * @ClassName: FastDFSClientWrapper
 * @date 2017/4/21
 * @version: V1.0
 */
@Component
public class FastDFSClientWrapper {
    private final Logger logger = LoggerFactory.getLogger(FastDFSClientWrapper.class);

    private final FastFileStorageClient storageClient;

    @Autowired
    public FastDFSClientWrapper(FastFileStorageClient storageClient) {
        this.storageClient = storageClient;
    }

    /**
     * 上传文件
     *
     * @param file 文件对象
     * @return 文件访问地址
     * @throws IOException
     */
    public String uploadFile(MultipartFile file) throws IOException {
        String fileExtName = FilenameUtils.getExtension(file.getOriginalFilename());
        long fileSize = file.getSize();
        StorePath storePath = storageClient.uploadFile(file.getInputStream(), fileSize, fileExtName, null);
        return "/" + storePath.getFullPath();
    }

    /**
     * 将本地文件复制到fastDFS上
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public String copyFileToFastDFS(String filePath) throws IOException {
        File file = new File(filePath);
        InputStream inputStream = new FileInputStream(file);
        String fileExtName = FilenameUtils.getExtension(file.getName());
        long fileSize = file.length();
        StorePath storePath = storageClient.uploadFile(inputStream, fileSize, fileExtName, null);
        inputStream.close();
        return "/" + storePath.getFullPath();

    }

    /**
     * 上传图片并生成缩略图
     *
     * @param file
     * @return
     * @throws IOException
     */
    public String uploadImageAndCrtThumbImage(MultipartFile file) throws IOException {
        StorePath storePath = storageClient.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(), FilenameUtils.getExtension(file.getOriginalFilename()), null);
        return storePath.getFullPath();
    }


    /**
     * 删除文件
     *
     * @param fileUrl 文件访问地址
     * @return
     */
    public void deleteFile(String fileUrl) {
        if (StringUtils.isEmpty(fileUrl)) {
            return;
        }
        try {
            StorePath storePath = StorePath.praseFromUrl(fileUrl);
            storageClient.deleteFile(storePath.getGroup(), storePath.getPath());
        } catch (FdfsUnsupportStorePathException e) {
            logger.warn(e.getMessage());
        }
    }
}
