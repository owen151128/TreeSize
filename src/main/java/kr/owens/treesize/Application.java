package kr.owens.treesize;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import kr.owens.treesize.util.FileUtil;

/**
 * @author owen151128@gmail.com
 * <p>
 * Created by owen151128 on 2019-08-05 13:53
 * <p>
 * Providing features related to Application class
 */
public class Application {

  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Input Arguments is invalid.");

      return;
    }

    Path rootPath = Paths.get(args[0]);

    if (rootPath == null) {
      System.out.println("Path is invalid.");

      return;
    }

    if (Files.notExists(rootPath)) {
      System.out.println("Path not found.");

      return;
    }

    try {
      ArrayList<Path> paths = FileUtil.getFileList(rootPath);
      Scanner scanner = new Scanner(System.in);
      int select = 0;

      while (!paths.isEmpty()) {
        select = Integer.parseInt(scanner.nextLine());

        if (select == 0) {
          break;
        }

        paths = FileUtil.getFileList(paths.get(select - 1));
      }
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    }
  }
}
