package com.haoyizebo.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Objects;

/**
 * @author yibo zhao
 * @date 2021/08/06
 */
public class SketchpadUtils {

    /**
     * 图片坐标
     */
    @Getter
    public static class ImagePosition {

        private final int x;
        private final int y;
        private final BufferedImage bufferedImage;
        private int width;
        private int height;

        public ImagePosition(int x, int y, BufferedImage bufferedImage) {
            this.x = x;
            this.y = y;
            this.bufferedImage = bufferedImage;
        }

        /**
         * 设置图片绘制为原样绘制
         */
        public ImagePosition withDefaultHeightAndWidth() {
            this.width = bufferedImage.getWidth();
            this.height = bufferedImage.getHeight();
            return this;
        }

        /**
         * 绘制图片为自定义大小绘制
         */
        public ImagePosition withResetHeightAndWidth(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }
    }

    /**
     * 创建一个图片坐标
     *
     * @param x             x
     * @param y             y
     * @param bufferedImage 图片
     * @return
     */
    public static ImagePosition createImagePosition(int x, int y, BufferedImage bufferedImage) {
        return new ImagePosition(x, y, bufferedImage);
    }

    /**
     * 文字坐标
     */
    @AllArgsConstructor
    @Getter
    private static class TextPosition {
        private int x;
        private final int y;
        private final Color color;
        private final Font font;
        private String text;

        /**
         * @param max 最大文字数量
         * @return
         */
        public TextPosition maxLength(int max) {
            int length = text.length();
            if (max < length) {
                this.text = this.text.substring(0, max);
            }
            return this;
        }

        public TextPosition center(int canvasWidth) {
            this.x = canvasWidth / 2 - this.text.length() * 9;
            return this;
        }
    }

    /**
     * 创建一个文字坐标
     *
     * @param x     x
     * @param y     y
     * @param text  文本
     * @param color 颜色
     * @param font  字体
     * @return
     */
    public static TextPosition createTextPosition(int x, int y, Color color, Font font, String text) {
        return new TextPosition(x, y, color, font, text);
    }

    /**
     * 读取图片到缓冲
     *
     * @param imgUrl 图片地址
     */
    public static BufferedImage readImg(String imgUrl) {
        try {
            return ImageIO.read(new URL(imgUrl));
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new NullPointerException("图片读取失败");
    }

    /**
     * 读取图片到缓冲
     *
     * @param file 图片文件
     */
    public static BufferedImage readImg(File file) {
        try {
            return ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new NullPointerException("图片读取失败");
    }

    /**
     * 创建指定大小的画布
     *
     * @param width  宽度
     * @param height 高度
     * @return
     */
    public static BufferedImage createCanvas(int width, int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    /**
     * 创建一个带背景的画布(平铺模式)
     *
     * @param url 图片地址
     * @return
     */
    public static BufferedImage createCanvas(String url) {
        BufferedImage background = readImg(url);
        BufferedImage canvas = new BufferedImage(background.getWidth(), background.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        drawImage((Graphics2D) canvas.getGraphics(), createImagePosition(0, 0, background).withDefaultHeightAndWidth());
        return canvas;
    }

    /**
     * 创建一个带背景的画布(圆角平铺模式)
     *
     * @param url 图片地址
     * @return
     */
    public static BufferedImage createFilletCanvas(String url) {
        BufferedImage background = readImg(url);
        int width = background.getWidth();
        int height = background.getHeight();
        // 透明底的图片
        BufferedImage canvas = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        setFillet(background, width, height, canvas);
        return canvas;
    }

    public static void setFillet(BufferedImage background, int width, int height, BufferedImage canvas) {
        Graphics2D graphics2D = canvas.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int border = 1;
        int resetWidth = width - border * 2;
        int resetHeight = height - border * 2;
        Ellipse2D.Double shape = new Ellipse2D.Double(border, border, resetWidth, resetHeight);
        graphics2D.setClip(shape);
        drawImage(graphics2D, createImagePosition(border, border, background).withResetHeightAndWidth(resetWidth, resetHeight));
        graphics2D.dispose();
        graphics2D = canvas.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        border = 4;
        Stroke s = new BasicStroke(1.5F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        graphics2D.setStroke(s);
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawOval(border, border, width - border * 2, width - border * 2);
        graphics2D.dispose();
    }

    /**
     * 绘制一个文本
     *
     * @param graphics2D 画板
     * @param text       文字坐标
     */
    public static void drawString(Graphics2D graphics2D, TextPosition text) {
        graphics2D.setColor(text.getColor());
        graphics2D.setFont(text.getFont());
        graphics2D.drawString(text.getText(), text.getX(), text.getY());
    }

    /**
     * 绘制一个图片
     *
     * @param graphics2D 画板
     * @param image      图片
     */
    @SuppressWarnings("all")
    public static void drawImage(Graphics2D graphics2D, ImagePosition image) {
        drawImage(graphics2D, image, Image.SCALE_AREA_AVERAGING);
    }

    /**
     * 绘制一个图片
     *
     * @param graphics2D            画板
     * @param image                 图片
     * @param imageScalingAlgorithm 图像缩放算法
     * @see java.awt.Image#SCALE_DEFAULT
     * @see java.awt.Image#SCALE_FAST
     * @see java.awt.Image#SCALE_SMOOTH
     * @see java.awt.Image#SCALE_REPLICATE
     * @see java.awt.Image#SCALE_AREA_AVERAGING
     */
    @SuppressWarnings("all")
    public static void drawImage(Graphics2D graphics2D, ImagePosition image, int imageScalingAlgorithm) {
        BufferedImage bufferedImage = image.getBufferedImage();
        if (Objects.isNull(image.getWidth()) || Objects.isNull(image.getHeight())) {
            image.withDefaultHeightAndWidth();
        }
        Image scaledInstance = bufferedImage.getScaledInstance(image.getWidth(), image.getHeight(), imageScalingAlgorithm);
        graphics2D.drawImage(scaledInstance, image.getX(), image.getY(), null);
    }

    private static InputStream write2InputStream(BufferedImage canvas) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(canvas, "png", os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(os.toByteArray());
    }

    public static void write2Local(BufferedImage canvas, String path) {
        try {
            ImageIO.write(canvas, "png", new FileOutputStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
