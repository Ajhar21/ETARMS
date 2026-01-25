package com.ztrios.etarms.employee.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.ztrios.etarms.common.exception.ExternalServiceException;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadEmployeeImage(@NotNull String employeeId, @NotNull MultipartFile file) {
        try {
            // Optional:can use employeeId or UUID for public_id
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
        // Cloudinary URL builder for v2
        return cloudinary.url()
                .transformation(new Transformation().width(150).height(150).crop("fill"))
                .generate("etarms/employees/" + employeeId); // include folder in publicId
    }
}
