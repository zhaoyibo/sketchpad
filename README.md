# sketchpad

## OverlayImage

**功能**：在一张图片上覆盖另一张图片

**场景**：批量处理实验素材——给个人资料页换头像 👉🏻❤️

**所需环境**：[JRE8](https://www.oracle.com/java/technologies/javase-jre8-downloads.html)

**使用说明**：

下载 [app.jar](https://github.com/zhaoyibo/sketchpad/blob/main/release/app.jar) 到任意目录下（例如 /tmp）

使用命令行进入该目录下

```shell
cd /tmp
```

执行

```shell
java -cp app.jar "com.haoyizebo.OverlayImage" --help
```

输出如下

```
Usage: <main class> [options]
  Options:
  * -b, --background
      【必填】背景图所在目录
  * -i, --image
      【必填】贴图所在目录
  * -o, --out
      【必填】输出目录（目录不存在时会自动创建）
    -l, --location
      贴图起始的坐标，格式：x,y（默认为 0,0）
    -s, --size
      贴图的尺寸，格式：width,height（默认为原始尺寸）
    --help
```

使用示例

```shell
# 批量：按 背景图目录×贴图目录 批量操作 
java -cp app.jar "com.haoyizebo.OverlayImage" \
  --background /Users/yibo/pics/background \
  --image /Users/yibo/pics/avatar \
  --out /Users/yibo/pics/out \
  --location 310,52 \
  --size 90,90
  
# 单个：按 背景图片＋贴图图片 单个操作
java -cp app.jar "com.haoyizebo.OverlayImage" \
  --background /Users/yibo/Downloads/pics/background/female.bmp \
  --image /Users/yibo/Downloads/pics/avatar/chinese/0002_real.bmp \
  --out /Users/yibo/Downloads/pics/out \
  --location 310,52 \
  --size 90,90
```

运行完毕后，即可在指定的 *out* 目录下看到处理好的图片