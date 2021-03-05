package com.marksemfileserver;

import com.marksemfileserver.config.PathConf;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.stream.Stream;

@Service
public class StorageService {

  @Autowired
  PathConf pathConf;

  public boolean isExist(String filename) {
    return Files.exists(Paths.get(this.pathConf.getPath() + "/" + filename));
  }

  public String save(MultipartFile file) {

    try {
      String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
      String originalFileName = resolveOriginalFileName(fileName, FilenameUtils.getExtension(file.getOriginalFilename()));
      Path targetLocation = this.pathConf.getPath().resolve(originalFileName);

      Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
      return originalFileName;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "";
  }

  public Resource get(String filename) throws FileNotFoundException {
    File outPutFile = new File(this.pathConf.getPath() + "/" + filename);
    return new InputStreamResource(new FileInputStream(outPutFile));
  }

  public boolean delete(String filename) {
    if (this.isExist(filename)) {
      try {
        Files.delete(Paths.get(this.pathConf.getPath() + "/" + filename));
      } catch (IOException e) {
        e.printStackTrace();
      }
      return true;
    } else return false;
  }

  private String resolveOriginalFileName(String fileName, String extension) {
    String[] filenameParts = fileName.split("__");
    System.out.println(filenameParts.length);
    if(filenameParts.length > 1) {
      return Stream.of(filenameParts)
          .limit(filenameParts.length - 1)
          .reduce((s1, s2) -> s1 + "__" + s2).get() + "." + extension;
    }
    return fileName;
  }

}
