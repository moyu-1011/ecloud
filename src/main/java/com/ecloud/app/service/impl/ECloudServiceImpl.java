package com.ecloud.app.service.impl;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.ecloud.app.common.ClientUtils;
import com.ecloud.app.enums.Sort;
import com.ecloud.app.pojo.StorageObject;
import com.ecloud.app.pojo.PictureInfo;
import com.ecloud.app.pojo.StorageObject;
import com.ecloud.app.service.ECloudService;
import com.ecloud.app.common.DateUtils;
import com.ecloud.app.common.SizeUtils;
import com.ecloud.app.common.SortUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class ECloudServiceImpl implements ECloudService {

    private static final String accessKey = "XLQXHDHOWE4Y3ASVA66G";
    private static final String accessSecret = "9nhzTLgQK9gkh41kHtJ4rsvX6YWrL22TyN0JXhCr";
    private static final String endpoint = "eos-chongqing-1.cmecloud.cn";
    private static final ClientConfiguration opts = new ClientConfiguration();
    private static final BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, accessSecret);
//    static AmazonS3Client client = null;
    private static final Logger logger = LoggerFactory.getLogger(ECloudServiceImpl.class);

//    public static AmazonS3Client getInstance() {
//        if (client == null) {
//            synchronized (ECloudService.class) {
//                if (client == null) {
//                    opts.setSignerOverride("S3SignerType");
//                    client = new AmazonS3Client(credentials, opts);
//                    client.setEndpoint(endpoint);
//                    logger.info("Initializing class: client ... ...");
//                }
//            }
//        } else
//            logger.info("client has been initialized...");
//        return client;
//    }

    @Override
    public List<PictureInfo> objectsGet(String bucketName) {
        AmazonS3Client client = ClientUtils.getInstance();

        final int maxKeys = 1000;// 最大返回数目
        GeneratePresignedUrlRequest request;
        ObjectListing objects = client.listObjects(new ListObjectsRequest().withBucketName(bucketName).withMaxKeys(maxKeys));
        List<S3ObjectSummary> summaries = objects.getObjectSummaries();
        ArrayList<PictureInfo> infoList = new ArrayList<>();
        for (S3ObjectSummary oss : summaries) {
            // bucketName
            String ossBucketName = oss.getBucketName();
            // keyName
            String ossKey = oss.getKey();
            request = new GeneratePresignedUrlRequest(bucketName, ossKey, HttpMethod.GET);
            // 过期时间为10分钟
            Date expiration = new Date(new Date().getTime() + 1000 * 60 * 10);
            request.setExpiration(expiration);
            // url
            URL url = client.generatePresignedUrl(request);
            // 大小
            long size = oss.getSize();
            // 最后修改日期
            Date lastModified = oss.getLastModified();
            PictureInfo info = new PictureInfo(ossKey, url.toString(), lastModified,
                    DateUtils.format(lastModified), size, SizeUtils.exchange(size), ossBucketName);
            infoList.add(info);
        }
        // 日期降序
        SortUtils.sort(infoList, Sort.DateDesc);
        return infoList;
    }

    @Override
    @Transactional
    public void objectsCopyAndDelete(List<StorageObject> objects) {
        // 对象复制
        objectsCopy(objects);

        // 删除原对象
        objectsDelete(objects);
    }

    @Override
    public void objectsCopy(List<StorageObject> listCopy) {
        AmazonS3Client client = ClientUtils.getInstance();

        CopyObjectRequest request;
        Iterator<StorageObject> iterator = listCopy.iterator();
        while (iterator.hasNext()) {
            StorageObject next = iterator.next();
            String sourceBucket = next.getSourceBucket();
            String keyName = next.getKeyName();
            String destinationBucket = next.getDestinationBucket();
            request = new CopyObjectRequest(sourceBucket, keyName, destinationBucket, keyName);
            // 将原bucket存进元信息,以便回收站复原
            if ("recycle-bin".equals(next.getDestinationBucket())) {
                HashMap<String, String> map = new HashMap<>();
                map.put("originalBucket", sourceBucket);
                // 图片后缀
                String suffix = keyName.substring(keyName.lastIndexOf(".") + 1);
                // 定义元信息'原bucket',从回收站复原时使用
                ObjectMetadata meta = new ObjectMetadata();
                meta.setUserMetadata(map);
                // 添加为图片格式
                meta.setContentType("image/" + suffix);
                request.withNewObjectMetadata(meta);
                client.copyObject(request);
            } else {
                client.copyObject(request);
            }
        }
    }


    @Override
    public void objectsDelete(List<StorageObject> listCopy) {
        AmazonS3Client client = ClientUtils.getInstance();

        for (StorageObject copy : listCopy) {
            DeleteObjectsRequest request = new DeleteObjectsRequest(copy.getSourceBucket());
            ArrayList<DeleteObjectsRequest.KeyVersion> listKeys = new ArrayList<>();
            DeleteObjectsRequest.KeyVersion keyVersion = new DeleteObjectsRequest.KeyVersion(copy.getKeyName());
            listKeys.add(keyVersion);
            request.setKeys(listKeys);
            client.deleteObjects(request);
        }
    }

    @Override
    public void objectsRecover(List<StorageObject> listCopy) {
        AmazonS3Client client = ClientUtils.getInstance();

        for (StorageObject copy : listCopy) {
            ObjectMetadata metadata = client.getObjectMetadata(copy.getSourceBucket(), copy.getKeyName());
            Map<String, String> map = metadata.getUserMetadata();
            String originalBucket = map.get("originalBucket");
            // 复原时,原bucket成为了目标bucket
            copy.setDestinationBucket(originalBucket);
        }
        objectsCopyAndDelete(listCopy);
    }

    @Override
    public byte[] objectsSave(List<StorageObject> objects) {
        AmazonS3Client client = ClientUtils.getInstance();

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(bao);
        for (int i = 0; i < objects.size(); i++) {
            try {
                StorageObject obj = objects.get(i);
                InputStream inputStream = objectGetAsStream(obj.getSourceBucket(), obj.getKeyName());
                zos.putNextEntry(new ZipEntry(obj.getKeyName()));
                byte[] bytes = FileCopyUtils.copyToByteArray(inputStream);
                zos.write(bytes);
                zos.closeEntry();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            // 必须手动刷新
            zos.finish();
            zos.close();
            bao.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bao.toByteArray();
    }


    @Override
    public InputStream objectGetAsStream(String bucketName, String keyName) {
        AmazonS3Client client = ClientUtils.getInstance();

        S3Object object = client.getObject(bucketName, keyName);

        return object.getObjectContent();
    }

    @Override
    public void objectUpload(String bucketName, String keyName, InputStream is, Long length) {
        AmazonS3Client client = ClientUtils.getInstance();
        ObjectMetadata meta = new ObjectMetadata();

        // 获取原本图片格式并上传
        String suffix = keyName.substring(keyName.lastIndexOf(".") + 1);
        meta.setContentType("image/" + suffix);

        // 对象大小
        meta.setContentLength(length);
        client.putObject(bucketName, keyName, is, meta);
    }
}
