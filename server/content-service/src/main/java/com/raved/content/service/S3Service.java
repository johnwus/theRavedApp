package com.raved.content.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * S3Service for TheRavedApp MongoDB with S3 storage
 */
public interface S3Service {

    /**
     * Upload file to S3
     */
    String uploadFile(MultipartFile file, String key);

    /**
     * Upload file from input stream
     */
    String uploadFile(InputStream inputStream, String key, String contentType, long size);

    /**
     * Download file from S3
     */
    InputStream downloadFile(String key);

    /**
     * Delete file from S3
     */
    void deleteFile(String key);

    /**
     * Get file URL
     */
    String getFileUrl(String key);

    /**
     * Get presigned URL for upload
     */
    String getPresignedUploadUrl(String key, String contentType);

    /**
     * Get presigned URL for download
     */
    String getPresignedDownloadUrl(String key);

    /**
     * Check if file exists
     */
    boolean fileExists(String key);

    /**
     * Get file metadata
     */
    FileMetadata getFileMetadata(String key);

    /**
     * List files in bucket
     */
    List<String> listFiles(String prefix);

    /**
     * Copy file within S3
     */
    void copyFile(String sourceKey, String destinationKey);

    /**
     * Move file within S3
     */
    void moveFile(String sourceKey, String destinationKey);

    /**
     * Generate presigned URL
     */
    String generatePresignedUrl(String key, long expirationInSeconds);

    /**
     * Update file metadata
     */
    void updateFileMetadata(String key, java.util.Map<String, String> metadata);

    /**
     * Get file size
     */
    long getFileSize(String key);

    /**
     * Get file content type
     */
    String getFileContentType(String key);

    /**
     * Create folder
     */
    void createFolder(String folderPath);

    /**
     * Delete folder
     */
    void deleteFolder(String folderPath);

    /**
     * File metadata class
     */
    class FileMetadata {
        private String key;
        private String contentType;
        private long size;
        private String etag;
        private String lastModified;

        // Getters and setters
        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        public String getEtag() {
            return etag;
        }

        public void setEtag(String etag) {
            this.etag = etag;
        }

        public String getLastModified() {
            return lastModified;
        }

        public void setLastModified(String lastModified) {
            this.lastModified = lastModified;
        }
    }
}
