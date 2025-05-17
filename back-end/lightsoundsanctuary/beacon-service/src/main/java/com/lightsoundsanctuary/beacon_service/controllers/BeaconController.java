package com.lightsoundsanctuary.beacon_service.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.UUID;

@RestController
@RequestMapping("/beacons")
public class BeaconController {
    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public BeaconController(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @PostMapping("/upload")
    public String uploadBeacon(@org.springframework.web.bind.annotation.RequestBody String jsonData) {
        String key = "beacon-" + UUID.randomUUID() + ".json";
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        s3Client.putObject(request, software.amazon.awssdk.core.sync.RequestBody.fromString(jsonData));
        return "Uploaded to S3: " + key;
    }
}
