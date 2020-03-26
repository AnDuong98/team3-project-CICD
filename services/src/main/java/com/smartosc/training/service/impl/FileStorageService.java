package com.smartosc.training.service.impl;


import com.smartosc.training.exception.FileStorageException;
import com.smartosc.training.exception.MyFileNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.smartosc.training.exception.MyFileNotFoundException.supplier;

@Service
public class FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    private final Path fileStorageLocation;

    @Value("${report.path}")
    private String reportPath;

    @Autowired
    public FileStorageService() {
        this.fileStorageLocation = Paths.get("E:\\smartosc\\jasper-report\\import")
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public Resource loadFileAsResource(String fileName){

        try {
            Path path = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(path.toUri());

            if (!resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new MyFileNotFoundException(supplier(fileName).get().getMessage());
            }

        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException(supplier(fileName).get().getMessage());
        }
    }


}
