package org.acme.file;

import io.minio.MinioAsyncClient;
import io.minio.PutObjectArgs;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.XmlParserException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@ApplicationScoped
public class SourceUpload
{

    @Inject
    MinioAsyncClient minio;

    private String bucket;

    private String object;

    public void setBucket(String bucket, String object) {
        this.bucket = bucket;
        this.object = object;
    }

    //메서드에 문자열을 전달 받도록 구현하여 현재 메서드 호출하여 객체 업로드
    public void uploadFile(String fileContent, String fileName)
    {

        byte[]      data   = fileContent.getBytes();
        InputStream stream = new ByteArrayInputStream(data);
        try {
            minio.putObject(PutObjectArgs.builder()
                                         .bucket(bucket)
                                         .object(fileName)
                                         .stream(stream, stream.available(), -1)
                                         .build());

        } catch (InvalidKeyException | IOException | InsufficientDataException | InternalException |
                 NoSuchAlgorithmException | XmlParserException e) {
            throw new RuntimeException(e);
        }
    }




}
