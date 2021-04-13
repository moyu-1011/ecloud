package com.ecloud.app.utils;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.*;
import com.amazonaws.services.s3.AmazonS3Client;
import com.ecloud.app.enums.Sort;
import com.ecloud.app.pojo.PictureInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class ECloudUtils {

    private static final String accessKey = "XLQXHDHOWE4Y3ASVA66G";
    private static final String accessSecret = "9nhzTLgQK9gkh41kHtJ4rsvX6YWrL22TyN0JXhCr";
    private static final String endpoint = "eos-chongqing-1.cmecloud.cn";
    private static final ClientConfiguration opts = new ClientConfiguration();
    private static final BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, accessSecret);

    public static void main(String[] args) throws IOException {

    }


    // 恢复
    public void objectRecover(String bucketName, String keyName) {
        AmazonS3Client client = null;
        opts.setSignerOverride("S3SignerType");
        client = new AmazonS3Client(credentials, opts);
        client.setEndpoint(endpoint);

        ObjectMetadata meta = client.getObjectMetadata(bucketName, keyName);
        Map<String, String> map = meta.getUserMetadata();
        String originalBucket = map.get("originalBucket");

        objectCopyAndDelete(bucketName, keyName, originalBucket, keyName);
    }

    // 流式上传
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


    //
    public byte[] stream() throws IOException {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(bao);

        // 文件写入数据
        for (int i = 0; i < 2; i++) {
            zos.putNextEntry(new ZipEntry(i + ".txt"));
            zos.write(String.valueOf(i).getBytes());
            zos.closeEntry();
        }

        // important !!! 不刷新导致数据丢失
        zos.finish();

        String stream = new String(bao.toByteArray(), StandardCharsets.ISO_8859_1);

        FileOutputStream fos = new FileOutputStream("F:\\1.zip");
        fos.write(stream.getBytes(StandardCharsets.ISO_8859_1));

        fos.close();
        zos.close();
        zos.flush();
        bao.close();

        return bao.toByteArray();
    }


    @Transactional
    public void objectCopyAndDelete(String sourceBucketName, String sourceKey, String destinationBucketName, String destinationKey) {
        // 对象复制
        objectCopy(sourceBucketName, sourceKey, destinationBucketName, destinationKey);

        // 删除原对象
        objectDelete(sourceBucketName, sourceKey);

    }


    // 删除
    public void objectDelete(String bucketName, String keys) {
        AmazonS3Client client = null;
        opts.setSignerOverride("S3SignerType");
        client = new AmazonS3Client(credentials, opts);
        client.setEndpoint(endpoint);

        client.deleteObject(bucketName, keys);
    }

    // 跨bucket复制对象
    public void objectCopy(String sourceBucketName, String sourceKey, String destinationBucketName, String destinationKey) {
        AmazonS3Client client = null;
        opts.setSignerOverride("S3SignerType");
        client = new AmazonS3Client(credentials, opts);
        client.setEndpoint(endpoint);

        CopyObjectRequest request;
        request = new CopyObjectRequest(sourceBucketName, sourceKey, destinationBucketName, destinationKey);

        HashMap<String, String> map = new HashMap<>();
        map.put("originalBucket", sourceBucketName);

        String suffix = sourceKey.substring(sourceKey.lastIndexOf(".") + 1);

        // 自定义元信息,将原bucket添加以便恢复
        ObjectMetadata meta = new ObjectMetadata();
        meta.setUserMetadata(map);
        meta.setContentType("image/" + suffix);

        request.withNewObjectMetadata(meta);
        client.copyObject(request);
    }

    // 批量获取 返回基本信息
    public List<PictureInfo> objectsGet(String bucketName) {
        AmazonS3Client client = null;
        opts.setSignerOverride("S3SignerType");
        client = new AmazonS3Client(credentials, opts);
        client.setEndpoint(endpoint);

        // 最大返回数目
        final int maxKeys = 1000;

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

//         日期降序
        SortUtils.sort(infoList, Sort.DateDesc);

        return infoList;
    }

    // 获取基本信息
//    public PictureInfo objectGet(String bucketName, String keyName) {
//        OSS client = ECloudClientUtils.getInstance();
//        SimplifiedObjectMeta meta = client.getSimplifiedObjectMeta(bucketName, keyName);
//        Date lastModified = meta.getLastModified();
//        long size = meta.getSize();
//        PictureInfo info = new PictureInfo(keyName, null, lastModified,
//                DateUtils.format(lastModified), size, SizeUtils.exchange(size), null);
//        return info;
//    }

    // 本地文件上传
//    public void nativeUpload(String bucketName, String keyName, String path) {
//        OSS client = ECloudClientUtils.getInstance();
//        PutObjectRequest request = new PutObjectRequest(bucketName, keyName, new File(path));
//        client.putObject(request);
//    }

    // 字节流上传
//    public void netUpload(String bucketName, String keyName, InputStream is) {
//        OSS client = ECloudClientUtils.getInstance();
//        ObjectMetadata meta = new ObjectMetadata();
//        // 获取后缀
//        String suffix = keyName.substring(keyName.lastIndexOf(".") + 1);
//        meta.setContentType("image/" + suffix);
////        client.putObject(bucketName, keyName, is, meta);
//    }

    // 单文件删除
//    public void objectDelete(String bucketName, String keyName) {
//        AmazonS3Client client = ECloudClientUtils.getInstance();
//        client.deleteObject(bucketName, keyName);
//    }


    // 压缩下载
    public byte[] objectsSave(String[] buckets, String[] keys) throws IOException {
        AmazonS3Client client = null;
        opts.setSignerOverride("S3SignerType");
        client = new AmazonS3Client(credentials, opts);
        client.setEndpoint(endpoint);

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(bao);

        for (int i = 0; i < buckets.length; i++) {
            S3Object object = client.getObject(buckets[i], keys[i]);

            InputStream is = object.getObjectContent();

            zos.putNextEntry(new ZipEntry(keys[i]));

            byte[] bytes = FileCopyUtils.copyToByteArray(is);
            zos.write(bytes);

            zos.closeEntry();
        }

        // important !!! 必须手动刷新,否则会造成数据不完整
        zos.finish();


        zos.close();
        bao.close();

        return bao.toByteArray();
    }

}
