package com.marksemfileserver;

import com.marksemfileserver.config.PathConf;
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

@Service
public class StorageService {

  @Autowired
  PathConf pathConf;

  public boolean isExist(String filename) {
    return Files.exists(Paths.get(this.pathConf.getPath() + "/" + filename));
  }

  public String save(MultipartFile file) {

    try {
      String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
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

  public boolean delete(String filename) throws IOException {
    if(this.isExist(filename)) {
      Files.delete(Paths.get(this.pathConf.getPath() + "/" + filename));
      return true;
    } else return false;
  }

  public String update (MultipartFile file) {
    return this.save(file);
  }

}
