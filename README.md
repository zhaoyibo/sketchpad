# sketchpad

## AvatarApp

批量处理实验素材：给个人资料页换头像 👉🏻❤️

![](https://user-images.githubusercontent.com/11988080/128552218-8f790042-009b-4a43-a0bd-6952dffb6618.png)

所需环境：[JRE8](https://www.oracle.com/java/technologies/javase-jre8-downloads.html)

在 *release* 中下载 *app.jar*

将 *app.jar* 移至图片目录下

假设当前目录的结构如下

```
.
├── app.jar        # 程序
├── avatar         # 头像图片目录
├── background     # 背景图片目录
└── processed      # 处理完的图片目录
```

进入该目录下，执行

```shell
java -cp app.jar "com.haoyizebo.AvatarApp" background avatar
```

运行完毕后，即可在 **processed** 目录下看到处理好的图片
