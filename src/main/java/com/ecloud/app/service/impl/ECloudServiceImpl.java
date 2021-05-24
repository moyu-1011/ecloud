package com.ecloud.app.service.impl;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.ecloud.app.enums.Sort;
import com.ecloud.app.pojo.ObjectCopy;
import com.ecloud.app.pojo.PictureInfo;
import com.ecloud.app.service.ECloudService;
import com.ecloud.app.util.DateUtils;
import com.ecloud.app.util.SizeUtils;
import com.ecloud.app.util.SortUtils;
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

    @Override
    public List<PictureInfo> objectsGet(String bucketName) {
        AmazonS3Client client = null;
        opts.setSignerOverride("S3SignerType");
        client = new AmazonS3Client(credentials, opts);
        client.setEndpoint(endpoint);

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
    public void objectsCopyAndDelete(List<ObjectCopy> listCopy) {
        // 对象复制
        objectsCopy(listCopy);

        // 删除原对象
        objectsDelete(listCopy);
    }

    @Override
    public void objectsCopy(List<ObjectCopy> listCopy) {
        AmazonS3Client client = null;
        opts.setSignerOverride("S3SignerType");
        client = new AmazonS3Client(credentials, opts);
        client.setEndpoint(endpoint);

        CopyObjectRequest request;
        Iterator<ObjectCopy> iterator = listCopy.iterator();
        while (iterator.hasNext()) {
            ObjectCopy next = iterator.next();
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
    public void objectsDelete(List<ObjectCopy> listCopy) {
        AmazonS3Client client = null;
        opts.setSignerOverride("S3SignerType");
        client = new AmazonS3Client(credentials, opts);
        client.setEndpoint(endpoint);

        for (ObjectCopy copy : listCopy) {
            DeleteObjectsRequest request = new DeleteObjectsRequest(copy.getSourceBucket());
            ArrayList<DeleteObjectsRequest.KeyVersion> listKeys = new ArrayList<>();
            DeleteObjectsRequest.KeyVersion keyVersion = new DeleteObjectsRequest.KeyVersion(copy.getKeyName());
            listKeys.add(keyVersion);
            request.setKeys(listKeys);
            client.deleteObjects(request);
        }
    }

    @Override
    public void objectsDetect(String bucketName, String keyname) {
        AmazonS3Client client = null;
        opts.setSignerOverride("S3SignerType");
        client = new AmazonS3Client(credentials, opts);
        client.setEndpoint(endpoint);

        DeleteObjectsRequest request = new DeleteObjectsRequest(bucketName);
        ArrayList<DeleteObjectsRequest.KeyVersion> listKeys = new ArrayList<>();
        DeleteObjectsRequest.KeyVersion keyVersion1 = new DeleteObjectsRequest.KeyVersion("01.jpg");
        DeleteObjectsRequest.KeyVersion keyVersion2 = new DeleteObjectsRequest.KeyVersion("04.jpg");
        listKeys.add(keyVersion1);
        listKeys.add(keyVersion2);
        request.setKeys(listKeys);
        client.deleteObjects(request);
    }

    @Override
    public void objectsRecover(List<ObjectCopy> listCopy) {
        AmazonS3Client client = null;
        opts.setSignerOverride("S3SignerType");
        client = new AmazonS3Client(credentials, opts);
        client.setEndpoint(endpoint);

        for (ObjectCopy copy : listCopy) {
            ObjectMetadata metadata = client.getObjectMetadata(copy.getSourceBucket(), copy.getKeyName());
            Map<String, String> map = metadata.getUserMetadata();
            String originalBucket = map.get("originalBucket");
            // 复原时,原bucket成为了目标bucket
            copy.setDestinationBucket(originalBucket);
        }
        System.out.println("---" + listCopy.toString());
        objectsCopyAndDelete(listCopy);
    }

    @Override
    public void objectsUpload(String bucketName, String keyName, InputStream is, Long length) {
        AmazonS3Client client = null;
        opts.setSignerOverride("S3SignerType");
        client = new AmazonS3Client(credentials, opts);
        client.setEndpoint(endpoint);

        ObjectMetadata meta = new ObjectMetadata();

        String suffix = keyName.substring(keyName.lastIndexOf(".") + 1);

        // 图片格式
        meta.setContentType("image/" + suffix);
        // 对象大小
        meta.setContentLength(length);

        client.putObject(bucketName, keyName, is, meta);

        client.shutdown();
    }


    @Override
    public byte[] objectsSave(String[] buckets, String[] keys) throws IOException {
        AmazonS3Client client = null;
        opts.setSignerOverride("S3SignerType");
        client = new AmazonS3Client(credentials, opts);
        client.setEndpoint(endpoint);

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(bao);
        for (int i = 0; i < buckets.length; i++) {
            InputStream stream = objectGetAsStream(buckets[i], keys[i]);
            zos.putNextEntry(new ZipEntry(keys[i]));
            byte[] bytes = FileCopyUtils.copyToByteArray(stream);
            zos.write(bytes);
            zos.closeEntry();
        }
        // important !!! 必须手动刷新,否则会造成数据不完整
        zos.finish();
        zos.close();
        bao.close();
        return bao.toByteArray();
    }

    @Override
    public InputStream objectGetAsStream(String bucketName, String keyName) {
        AmazonS3Client client = null;
        opts.setSignerOverride("S3SignerType");
        client = new AmazonS3Client(credentials, opts);
        client.setEndpoint(endpoint);

        S3Object object = client.getObject(bucketName, keyName);
        InputStream stream = object.getObjectContent();
        return stream;
    }

    @Override
    public void objectUpload(String bucketName, String keyName, InputStream is, Long length) {
        AmazonS3Client client = null;
        opts.setSignerOverride("S3SignerType");
        client = new AmazonS3Client(credentials, opts);
        client.setEndpoint(endpoint);

        ObjectMetadata meta = new ObjectMetadata();

        String suffix = keyName.substring(keyName.lastIndexOf(".") + 1);

        // 图片格式
        meta.setContentType("image/" + suffix);
        // 对象大小
        meta.setContentLength(length);

        client.putObject(bucketName, keyName, is, meta);

        client.shutdown();
    }
}
