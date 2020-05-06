package io.hellgren.wiremockdemo;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

public class FileHelper {

  public static String readFileContent(String resourcePath) {
    var resource = new ClassPathResource(resourcePath);
    try {
      var reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
      return FileCopyUtils.copyToString(reader);
    } catch (IOException e) {
      throw new FailedToReadSqlFileException(e);
    }
  }

  private static class FailedToReadSqlFileException extends RuntimeException {
    public FailedToReadSqlFileException(IOException e) {
      super(e);
    }
  }
}
