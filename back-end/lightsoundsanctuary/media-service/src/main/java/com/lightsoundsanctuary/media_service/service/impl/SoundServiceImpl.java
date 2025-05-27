package com.lightsoundsanctuary.media_service.service.impl;

import com.lightsoundsanctuary.media_service.entity.Category;
import com.lightsoundsanctuary.media_service.entity.Sound;
import com.lightsoundsanctuary.media_service.repository.CategoryRepository;
import com.lightsoundsanctuary.media_service.repository.SoundRepository;
import com.lightsoundsanctuary.media_service.service.SoundService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SoundServiceImpl implements SoundService {

    private final SoundRepository soundRepository;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${aws.access.key}")
    private String accessKey;

    @Value("${aws.secret.key}")
    private String secretKey;

    @Value("${aws.region}")
    private String region;

    @Override
    public List<Sound> getAllSounds() {
        return soundRepository.findAll();
    }

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Sound uploadSound(String title, String categoryName, MultipartFile file) throws IOException {
        String key = "sounds/" + UUID.randomUUID() + "-" + file.getOriginalFilename();

        S3Client s3 = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .build();

        s3.putObject(PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .contentType(file.getContentType())
                        .build(),
                RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        String s3Url = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, key);

        // 🔎 Look up the Category entity
        Category category = categoryRepository.findByNameIgnoreCase(categoryName)
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + categoryName));

        // ✅ Create the sound with Category object
        Sound sound = new Sound(null, title, s3Url, category);
        return soundRepository.save(sound);
    }

}
