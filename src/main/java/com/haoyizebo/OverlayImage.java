package com.haoyizebo;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.haoyizebo.util.FileUtils;
import com.haoyizebo.util.SketchpadUtils;
import lombok.Data;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author yibo zhao
 * @date 2021/08/06
 */
public class OverlayImage {

    @Data
    public static class OverlayImageArgs {

        @Parameter(names = {"-b", "--background"}, description = "【必填】背景图所在目录", required = true, order = 1)
        public String backgroundDir;
        @Parameter(names = {"-i", "--image"}, description = "【必填】贴图所在目录", required = true, order = 2)
        public String imageDir;
        @Parameter(names = {"-o", "--out"}, description = "【必填】输出目录（目录不存在时会自动创建）", required = true, order = 3)
        public String outDir;

        @Parameter(names = {"-l", "--location"}, description = "贴图起始的坐标，格式：x,y（默认为 0,0）", order = 4)
        public String location;
        @Parameter(names = {"-s", "--size"}, description = "贴图的尺寸，格式：width,height（默认为原始尺寸）", order = 5)
        public String size;

        @Parameter(names = {"--scale"}, description = "图像缩放算法，取值（默认为 16）：\n" +
                "        1\tDEFAULT\t\t\t默认缩放算法\n" +
                "        2\tFAST\t\t\t速度优先算法\n" +
                "        4\tSMOOTH\t\t\t平滑优先算法\n" +
                "        8\tREPLICATE\t\t像素复制型缩放算法\n" +
                "        16\tAREA_AVERAGING\t\t区域均值算法", order = 6)
        public int scale = 16;

        @Parameter(names = "--help", help = true, order = 6)
        private boolean help;

    }

    public static void main(String[] args) throws IOException {

        OverlayImageArgs overlayImageArgs = new OverlayImageArgs();
        JCommander jCommander = JCommander.newBuilder().addObject(overlayImageArgs).build();
        jCommander.parse(args);

        if (overlayImageArgs.help) {
            usage(jCommander, null);
            return;
        }

        String size = overlayImageArgs.getSize();
        int width = 0, height = 0;
        if (isNotBlank(size)) {
            String[] split = size.trim().split(",");
            try {
                width = Integer.parseInt(split[0]);
                height = Integer.parseInt(split[1]);
            } catch (Exception e) {
                usage(jCommander, "贴图尺寸有误");
                return;
            }
        }

        String location = overlayImageArgs.getLocation();
        int x = 0, y = 0;
        if (isNotBlank(location)) {
            String[] split = location.trim().split(",");
            try {
                x = Integer.parseInt(split[0]);
                y = Integer.parseInt(split[1]);
            } catch (Exception e) {
                usage(jCommander, "贴图起始的坐标有误");
                return;
            }
        }

        File bFile = new File(overlayImageArgs.getBackgroundDir());
        File pFile = new File(overlayImageArgs.getImageDir());
        if (!bFile.exists() || !pFile.exists()) {
            usage(jCommander, "目录或文件不存在：背景图|贴图");
            return;
        }
        File oFile = new File(overlayImageArgs.getOutDir());
        if (oFile.isFile() && oFile.exists()) {
            usage(jCommander, "输出目录是个文件，且已存在");
            return;
        }

        System.out.println("开始处理...");

        int finalX = x, finalY = y;
        int finalWidth = width, finalHeight = height;
        // 从背景图开始遍历
        Files.walk(Paths.get(overlayImageArgs.getBackgroundDir()))
                .filter(Files::isRegularFile)
                .forEach(backgroundPath -> {
                    BufferedImage background = SketchpadUtils.readImg(backgroundPath.toFile());
                    if (background == null) {
                        return;
                    }

                    // 从贴图开始遍历
                    try {
                        Path imageRootPath = Paths.get(overlayImageArgs.getImageDir());
                        Files.walk(imageRootPath)
                                .filter(Files::isRegularFile)
                                .forEach(imagePath -> {

                                    BufferedImage pic = SketchpadUtils.readImg(imagePath.toFile());
                                    if (pic == null) {
                                        return;
                                    }

                                    // 在背景图上覆盖贴图
                                    SketchpadUtils.ImagePosition imagePosition = SketchpadUtils.createImagePosition(finalX, finalY, pic)
                                            .withResetHeightAndWidth(finalWidth > 0 ? finalWidth : pic.getWidth(), finalHeight > 0 ? finalHeight : pic.getHeight());
                                    SketchpadUtils.drawImage(background.createGraphics(), imagePosition, overlayImageArgs.getScale());

                                    // 计算存储路径
                                    String backgroundImageName = FileUtils.trimExtension(backgroundPath.getFileName().toString());
                                    String toSaveImageName = FileUtils.trimExtension(imagePath.getFileName().toString());
                                    Path relativizePath = imageRootPath.relativize(imagePath).getParent();
                                    File toSaveDir = Paths.get(overlayImageArgs.getOutDir(), backgroundImageName, relativizePath == null ? "" : relativizePath.toString()).toFile();
                                    // 自动创建文件夹
                                    if (!toSaveDir.exists()) {
                                        boolean mkdirs = toSaveDir.mkdirs();
                                    }
                                    // 写磁盘
                                    SketchpadUtils.write2Local(background, Paths.get(toSaveDir.getPath(), toSaveImageName + ".png").toString());
                                });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                });

        System.out.println("处理完成，请检查 " + overlayImageArgs.getOutDir() + " 文件夹。");

    }

    private static void usage(JCommander jCommander, String errMsg) {
        if (isNotBlank(errMsg)) {
            System.err.println("ERROR: " + errMsg);
        }
        jCommander.setColumnSize(1000);
        jCommander.usage();
    }

    private static boolean isNotBlank(String str) {
        return str != null && str.trim().length() > 0;
    }


}
