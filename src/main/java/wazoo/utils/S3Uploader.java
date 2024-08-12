package wazoo.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.UUID;

@Slf4j
@Service
public class S3Uploader {

    private final S3Client s3Client;
    private final String bucket;
    private final Region region;

    public S3Uploader(S3Client s3Client, @Value("${cloud.aws.s3.bucket}") String bucket, @Value("${cloud.aws.region.static}") String region) {
        this.s3Client = s3Client;
        this.bucket = bucket;
        this.region = Region.of(region);
    }

    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        String originalFileName = multipartFile.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String uniqueFileName = uuid + "_" + originalFileName.replaceAll("\\s", "_");

        String fileName = dirName + "/" + uniqueFileName;
        log.info("fileName: " + fileName);
        File uploadFile = convert(multipartFile);

        String uploadImageUrl = putS3(uploadFile, fileName);
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    private File convert(MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String uniqueFileName = uuid + "_" + originalFileName.replaceAll("\\s", "_");

        File convertFile = new File(uniqueFileName);
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            } catch (IOException e) {
                log.error("파일 변환 중 오류 발생: {}", e.getMessage());
                throw e;
            }
            return convertFile;
        }
        throw new IllegalArgumentException(String.format("파일 변환에 실패했습니다. %s", originalFileName));
    }

    private String putS3(File uploadFile, String fileName) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .build();

        PutObjectResponse response = s3Client.putObject(putObjectRequest, uploadFile.toPath());

        if (response.sdkHttpResponse().isSuccessful()) {
            return "https://" + bucket + ".s3." + region.id() + ".amazonaws.com/" + fileName;
        } else {
            throw new RuntimeException("S3 업로드 실패: " + response.sdkHttpResponse().statusText().orElse("unknown error"));
        }
    }

    private void removeNewFile(File targetFile) {
        try {
            Files.delete(targetFile.toPath());
            log.info("파일이 삭제되었습니다.");
        } catch (IOException e) {
            log.error("파일 삭제 중 오류 발생: {}", e.getMessage());
        }
    }

    public void deleteFile(String fileName) {
        try {
            String decodedFileName = URLDecoder.decode(fileName, "UTF-8");
            log.info("Deleting file from S3: " + decodedFileName);

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(decodedFileName)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
        } catch (IOException e) {
            log.error("Error while decoding the file name: {}", e.getMessage());
        }
    }

    public String updateFile(MultipartFile newFile, String oldFileName, String dirName) throws IOException {
        log.info("S3 oldFileName: " + oldFileName);
        deleteFile(oldFileName);
        return upload(newFile, dirName);
    }
}
