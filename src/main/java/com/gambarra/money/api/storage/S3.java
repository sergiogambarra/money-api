package com.gambarra.money.api.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.gambarra.money.api.config.property.MoneyApiProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

@Component
public class S3 {

    private static final Logger logger = LoggerFactory.getLogger(S3.class);

    @Autowired
    private MoneyApiProperty property;

    @Autowired
    private AmazonS3 amazonS3;

    public String salvarTemporatiamente(MultipartFile file) throws IOException {
        AccessControlList accessControlList = new AccessControlList();
        accessControlList.grantPermission(GroupGrantee.AllUsers, Permission.Read);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());

        String uniqueName = generateUniqueName(file.getOriginalFilename());

        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    property.getS3().getBucket(),
                    uniqueName,
                    file.getInputStream(),
                    objectMetadata)
                    .withAccessControlList(accessControlList);

            putObjectRequest.setTagging(new ObjectTagging(
                    Arrays.asList(new Tag("expirar", "true"))
            ));

            amazonS3.putObject(putObjectRequest);

            if (logger.isDebugEnabled()){
                logger.debug("Arquivo enviado com sucesso para o S3", file.getOriginalFilename());
            }
        } catch (IOException e) {
            throw new RuntimeException("Problemas ao tentar enviar o arquivo para o S3");
        }

        return uniqueName;

    }

    public String configureuUrl(String object) {
        return "\\\\" + property.getS3().getBucket() +
                "s3.amazonaws.com/" + object;
    }

    public void save(String object) {
        SetObjectTaggingRequest setObjectTaggingRequest = new SetObjectTaggingRequest(
                property.getS3().getBucket(),
                object,
                new ObjectTagging(Collections.emptyList()));

        amazonS3.setObjectTagging(setObjectTaggingRequest);
    }

    public void remove(String object) {
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(
                property.getS3().getBucket(), object);

        amazonS3.deleteObject(deleteObjectRequest);
    }

    public void replace(String oldObject, String newObject) {
        if (StringUtils.hasText(oldObject)) {
            this.remove(oldObject);
        }
        save(newObject);
    }

    private String generateUniqueName(String originalFilename) {
        return UUID.randomUUID().toString() + "_" + originalFilename;
    }



}

