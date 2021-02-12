package com.marksemfileserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Objects;


@RestController
public class FileUploadController {

  @Autowired
  private StorageService storageService;

  @PostMapping("/uploadFile")
  public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {

    if (StringUtils
        .cleanPath(Objects.requireNonNull(file.getOriginalFilename()))
        .contains("..")
    ) {
      return ResponseEntity.status(415)
          .body("Invalid file name. File name contains unsupported characters\"..\"");
    }

    String fileName = storageService.save(file);

    return ResponseEntity.ok(ServletUriComponentsBuilder.fromCurrentContextPath()
        .path("getFile/")
        .path(fileName)
        .toUriString());

  }

  @GetMapping("/getFile/{filename}")
  public ResponseEntity<Resource> getFileByName(@PathVariable String filename) throws IOException {

    if(this.storageService.isExist(filename)) {

      return ResponseEntity.ok()
          .contentLength(storageService.get(filename).contentLength())
          .contentType(MediaType.APPLICATION_OCTET_STREAM)
          .body(storageService.get(filename));

    } else {
      return ResponseEntity.status(404)
          .body(new InputStreamResource(new ByteArrayInputStream("File not found".getBytes())));
    }
  }

  @DeleteMapping("/deleteFile/{filename}")
  public ResponseEntity<?> deleteFileByName(@PathVariable String filename)  {
    if (this.storageService.delete(filename)) {
      return ResponseEntity.ok().body("deletedFile: " + filename);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("deletedFile: not found");
    }
  }

  @PutMapping("/updateFile")
  public ResponseEntity<?> updateFile(@RequestParam("file") MultipartFile file) {
    return this.uploadFile(file);
  }

}