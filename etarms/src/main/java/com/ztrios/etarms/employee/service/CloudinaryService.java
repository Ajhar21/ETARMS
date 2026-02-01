package com.ztrios.etarms.employee.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.ztrios.etarms.common.exception.ExternalServiceException;
import com.ztrios.etarms.common.exception.InvalidFileException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public String uploadEmployeeImage(@NotNull String employeeId, @NotNull MultipartFile file) {
        try {
            // Optional: can use employeeId or UUID for public_id
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", "etarms/employees",
                    "public_id", employeeId,
                    "overwrite", true,  // overwrite same employee image
                    "resource_type", "image"
            ));
            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new ExternalServiceException(
                    "Image upload service is currently unavailable",
                    e
            );
        }
    }

    public String getThumbnailUrl(String employeeId) {

        return cloudinary.url()
                .transformation(new Transformation().width(150).height(150).crop("fill"))
                .generate("etarms/employees/" + employeeId); // include folder in publicID
    }

    public void validateImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new InvalidFileException("File is empty");
        }

        if (file.getSize() > 2 * 1024 * 1024) {
            throw new InvalidFileException("File size exceeds 2MB");
        }

        try (InputStream is = file.getInputStream()) {
            Tika tika = new Tika();
            String detectedType = tika.detect(is);

            //log.warn("Detected content type: {}", detectedType);

            if (!"image/jpeg".equals(detectedType) && !"image/png".equals(detectedType)) {
                throw new InvalidFileException("Only JPEG or PNG images are allowed");
            }
        } catch (IOException e) {
            throw new InvalidFileException("Failed to read file content");
        }
    }
}
