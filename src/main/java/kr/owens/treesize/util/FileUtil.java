package kr.owens.treesize.util;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 * @author owen151128@gmail.com
 * <p>
 * Created by owen151128 on 2019-08-05 14:04
 * <p>
 * Providing features related to FileUtil class
 */
public class FileUtil {

  private static final double KB_SIZE = 1024.0;
  private static final double MB_SIZE = 1048576.0;
  private static final double GB_SIZE = 1073741824.0;
  private static final double UNIT_DOUBLE = 100.0;

  private static final long UNIT_LONG = 100;

  private static final String BYTE = " BYTES";
  private static final String KB = " KB";
  private static final String MB = " MB";
  private static final String GB = " GB";

  private static final String CAST_FAILED = "File Size cast failed! current size : %d";
  private static final String GET_FAILED = "File Size calculate failed! current file : %s";

  private static long dirFileSize = 0;
  private static int fileIndex = 1;

  private static String getCurrentCastedFileSize(Path targetFile) {
    long fileSize = 0;
    dirFileSize = 0;

    try {
      if (Files.isDirectory(targetFile)) {
        Files.walkFileTree(targetFile, new SimpleFileVisitor<Path>() {
          @Override
          public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
              throws IOException {
            dirFileSize += Files.size(file);
            return super.visitFile(file, attrs);
          }
        });
        fileSize = dirFileSize;
      } else {

        fileSize = Files.size(targetFile);
      }
    } catch (IOException e) {
      throw new IllegalArgumentException(
          String.format(GET_FAILED, targetFile.toAbsolutePath().toString()));
    }

    if (fileSize < 1024) {
      return fileSize + BYTE;
    }

    if (fileSize > GB_SIZE) {
      double convertSize = fileSize / GB_SIZE;

      return Math.round(convertSize * UNIT_LONG) / UNIT_DOUBLE + GB;
    }

    if (fileSize > MB_SIZE) {
      double convertSize = fileSize / MB_SIZE;

      return Math.round(convertSize * UNIT_LONG) / UNIT_DOUBLE + MB;
    }

    if (fileSize > KB_SIZE) {
      double convertSize = fileSize / KB_SIZE;

      return Math.round(convertSize * UNIT_LONG) / UNIT_DOUBLE + KB;
    }

    throw new IllegalArgumentException(String.format(CAST_FAILED, fileSize));
  }

  public static ArrayList<Path> getFileList(Path root) {
    ArrayList<Path> paths = new ArrayList<>();
    fileIndex = 1;

    try {
      Stream<Path> list = Files.list(root);
      StringBuilder sb = new StringBuilder();

      list.forEach(p -> {
        if (Files.isDirectory(p)) {
          paths.add(p);
          System.out.println(
              fileIndex++ + " : " + p.getFileName().toString() + " / size : "
                  + getCurrentCastedFileSize(
                  p));
        } else {
          sb.append(p.getFileName().toString() + " / size : " + getCurrentCastedFileSize(p) + "\n");
        }
      });

      System.out.print(sb.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }

    return paths;
  }
}
