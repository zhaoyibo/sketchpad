package com.haoyizebo;

import com.haoyizebo.util.FileUtils;
import com.haoyizebo.util.SketchpadUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author yibo zhao
 * @date 2021/08/06
 */
public class AvatarApp {

    public static void main(String[] args) throws IOException {

        if (args.length != 2) {
            System.err.println("1. 请在要处理的图片目录下运行。");
            System.err.println("2. 例如当前目录下有两个文件夹：背景文件夹background 与 头像文件夹avatar，则在当前目录执行：");
            System.err.println("      java -cp app.jar \"com.haoyizebo.AvatarApp\" background avatar");
            System.err.println("3. 处理完的图片位于 processed 文件夹下");
            System.exit(1);
        }

        System.out.println("开始处理...");

        String backgroundArg = args[0];
        String avatarArg = args[1];

        String baseDir = "";

        Files.walk(Paths.get(baseDir, backgroundArg))
                .filter(Files::isRegularFile)
                .forEach(bPath -> {
                    BufferedImage background = SketchpadUtils.readImg(bPath.toFile());
                    if (background == null) {
                        return;
                    }

                    try {
                        Files.walk(Paths.get(baseDir, avatarArg))
                                .filter(Files::isRegularFile)
                                .forEach(aPath -> {

                                    BufferedImage avatar = SketchpadUtils.readImg(aPath.toFile());
                                    if (avatar == null) {
                                        return;
                                    }

                                    String backgroundDir = FileUtils.trimExtension(bPath.getFileName().toString());
                                    String categoryDir = aPath.getParent().getFileName().toString();
                                    String fileName = FileUtils.trimExtension(aPath.getFileName().toString());

                                    SketchpadUtils.ImagePosition imagePosition = SketchpadUtils.createImagePosition(310, 52, avatar).withResetHeightAndWidth(90, 90);
                                    SketchpadUtils.drawImage(background.createGraphics(), imagePosition);

                                    File saveDir = Paths.get(baseDir, "processed", backgroundDir, categoryDir).toFile();
                                    if (!saveDir.exists()) {
                                        saveDir.mkdirs();
                                    }
                                    SketchpadUtils.write2Local(background, Paths.get(saveDir.getPath(), fileName + ".png").toString());
                                });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                });

        System.out.println("处理完成，请检查 processed 文件夹。");

    }


}
