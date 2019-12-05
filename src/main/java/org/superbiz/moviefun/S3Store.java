package org.superbiz.moviefun;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.superbiz.moviefun.blobstore.Blob;
import org.superbiz.moviefun.blobstore.BlobStore;
import org.superbiz.moviefun.blobstore.ServiceCredentials;

import java.io.IOException;
import java.util.Optional;

public class S3Store implements BlobStore {


    private AmazonS3Client s3Client;


    private String photoStorageBucket;


    @Autowired
    ServiceCredentials serviceCredentials;


    public S3Store(AmazonS3Client s3Client, String photoStorageBucket) {
        this.s3Client = s3Client;
        this.photoStorageBucket = photoStorageBucket;
    }


    @Override
    public void put(Blob blob) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(blob.getContentType());
        s3Client.putObject(photoStorageBucket, blob.getName(), blob.getInputStream(), objectMetadata);
    }

    @Override
    public Optional<Blob> get(String name) throws IOException {
        try {
            S3Object s3Object = s3Client.getObject(serviceCredentials.getCredential("photo-storage", "user-provided", "bucket"), name);
            return getBlob(s3Object);
        } catch (AmazonS3Exception e) {
                S3Object s3Object = s3Client.getObject(serviceCredentials.getCredential("photo-storage", "user-provided", "bucket"), "covers/sunset.jpg");
                return getBlob(s3Object);
        }

    }

    private Optional<Blob> getBlob(S3Object s3Object) {
        Blob resultBlob = new Blob();
        resultBlob.setName(s3Object.getKey());
        resultBlob.setContentType(s3Object.getObjectMetadata().getContentType());
        resultBlob.setInputStream(s3Object.getObjectContent());
        return Optional.of(resultBlob);
    }


    @Override
    public void deleteAll() {


    }
}
