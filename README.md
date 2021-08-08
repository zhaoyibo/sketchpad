# sketchpad

## OverlayImage

**功能**：在一张图片上覆盖另一张图片

**场景**：批量处理实验素材——给个人资料页换头像 👉🏻❤️

<img width="200" alt="example" src="https://user-images.githubusercontent.com/11988080/128552218-8f790042-009b-4a43-a0bd-6952dffb6618.png">

**所需环境**：[JRE8](https://www.oracle.com/java/technologies/javase-jre8-downloads.html)

**使用说明**：

下载 [app.jar](https://github.com/zhaoyibo/sketchpad/raw/main/release/app.jar) 到任意目录下

使用命令行进入该目录下

```shell
# 假设 app.jar 在 /tmp 目录下
cd /tmp
```

执行

```shell
java -cp app.jar "com.haoyizebo.OverlayImage" --help
```

可看到使用文档

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
    --scale
      图像缩放算法，取值（默认为 16）：
        1	DEFAULT		默认缩放算法
        2	FAST		速度优先算法
        4	SMOOTH		平滑优先算法
        8	REPLICATE	像素复制型缩放算法
        16	AREA_AVERAGING	区域均值算法
      Default: 16
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
  -b /Users/yibo/Downloads/pics/background/female.bmp \
  -i /Users/yibo/Downloads/pics/avatar/chinese/0002_real.bmp \
  -o /Users/yibo/Downloads/pics/out \
  -l 310,52 \
  -s 90,90
```

运行完毕后，即可在指定的 *out* 目录下看到处理好的图片

**图像缩放算法对比**：

测试图像原始尺寸为 100\*100px，缩放为 90\*90px，使用 「4 SMOOTH 平滑优先算法」或「16 AREA_AVERAGING 区域均值算法」效果较好


| DEFAULT    | FAST    | SMOOTH    |
| ---- | ---- | ---- |
|  <img width="200" alt="DEFAULT" src="https://user-images.githubusercontent.com/11988080/128625443-dfdda96d-626b-4c75-98b4-24b8046313e9.png">  | <img width="200" alt="FAST" src="https://user-images.githubusercontent.com/11988080/128625453-4070d8c2-08af-4967-ba73-9fca4250c052.png">    | <img width="200" alt="SMOOTH" src="https://user-images.githubusercontent.com/11988080/128625459-5b59f3e7-bb03-4b5a-bc94-4909d62f8e7a.png">    |

| REPLICATE    | AREA_AVERAGING    | 
| ---- | ---- |
| <img width="200" alt="REPLICATE" src="https://user-images.githubusercontent.com/11988080/128625465-54f5c832-72ac-4402-a692-d25b688d3149.png">    | <img width="200" alt="AREA_AVERAGING" src="https://user-images.githubusercontent.com/11988080/128625471-60aa9b39-857a-44c8-baae-ed6a1ffab6d1.png">   | 





