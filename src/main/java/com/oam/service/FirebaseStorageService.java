package com.oam.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import com.oam.exception.ErrorMessage;
import com.oam.exception.model.InternalServerErrorException;
import com.oam.util.FirebaseConstants;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

@Service
public class FirebaseStorageService {

    public URL uploadFile(String imageName, MultipartFile image, String contentType) throws IOException {
        Blob blob = getDefaultBucket().create(imageName, image.getInputStream(), contentType);
        BlobInfo blobInfo = BlobInfo.newBuilder(System.getenv(FirebaseConstants.BUCKET_NAME), blob.getName()).build();
        return getDefaultBucket().getStorage().signUrl(blobInfo, 365 * 25, TimeUnit.DAYS);
    }

    private Bucket getDefaultBucket() {
        try {
            String bucketName = System.getenv(FirebaseConstants.BUCKET_NAME);
            InputStream serviceAccount = new ByteArrayInputStream(System.getenv(FirebaseConstants.PERMISSIONS).getBytes());

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setStorageBucket(bucketName)
                        .build();

                FirebaseApp.initializeApp(options);
            }

            return StorageClient.getInstance().bucket();
        } catch (IOException e) {
            throw new InternalServerErrorException(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
