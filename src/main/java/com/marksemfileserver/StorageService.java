package com.marksemfileserver;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class StorageService {

  private final Path fileStorageLocation = Paths.get("C:/Users/Eduard/IdeaProjects/file_server_final-fs11/media/upload");

  public boolean isExist(String filename) {
    return Files.exists(Paths.get(this.fileStorageLocation + "/" + filename));
  }

  public String save(MultipartFile file) {

    try {
      String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
      Path targetLocation = fileStorageLocation.resolve(originalFileName);

      Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
      return originalFileName;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "";
  }

  public Resource get(String filename) throws FileNotFoundException {
    File outPutFile = new File(this.fileStorageLocation + "/" + filename);
    return new InputStreamResource(new FileInputStream(outPutFile));
  }

  public boolean delete(String filename) throws IOException {
    if(this.isExist(filename)) {
      Files.delete(Paths.get(this.fileStorageLocation + "/" + filename));
      return true;
    } else return false;
  }

}
