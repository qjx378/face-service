<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">人脸识别服务</h1>
<h4 align="center">基于Spring Boot服务架构</h4>
<p align="center">
  <a href="https://gitee.com/qjx378/face-service/stargazers"><img src="https://gitee.com/qjx378/face-service/badge/star.svg?theme=dark"></a>
  <a href="https://gitee.com/qjx378/face-service"><img src="https://img.shields.io/badge/FaceService-v0.0.1-brightgreen.svg"></a>
  <a href="https://gitee.com/qjx378/face-service/blob/master/LICENSE"><img src="https://img.shields.io/github/license/qjx378/face-service"></a>

</p>

## **简介**
基于开源人脸检测AI模型，通过利用Java技术和向量搜索技术提供包括人脸检测与分析、比对、搜索、验证、五官定位、活体检测等API接口服务功能，为开发者和企业提供高性能高可用的人脸识别服务。可应用于在线娱乐、在线身份认证等多种应用场景，充分满足各行业客户的人脸属性识别及用户身份确认等需求。
<br><br>
关于中科视拓开源人脸模型（SeetaFace6），您可以访问[https://github.com/SeetaFace6Open/index](https://github.com/SeetaFace6Open/index)了解更多。
<br>
SeetaFace6入门教程[https://github.com/seetafaceengine/SeetaFaceTutorial](https://github.com/seetafaceengine/SeetaFaceTutorial)了解更多。

## **技术架构**
基于Spring Boot+MySQL+Milvus的技术组合，接口采用RESTful API定义，通过JNI（Java Native Interface）技术调用底层的人脸识别模型，实现人脸的检测与识别。

## **接口范围**
本API服务主要专注于提供包括人脸检测与分析、比对、搜索、验证、五官定位、活体检测等API接口服务，在该API服务中实现人脸检测与分析、五官定位、人脸比对、人脸库管理、人脸搜索、人脸静态活体检测，主要包括如下API对外接口：

1）人脸检测与分析<br>
对请求图片检测返回人脸的位置、面部属性；对请求图片进行五官定位，计算构成人脸轮廓的68个点

2）人脸比对<br>
对两张图片中的人脸进行相似度比对，返回人脸相似度分数。

3）人脸库管理<br>
包括增、删人脸库，人脸库增、删人脸。

3）人脸搜索<br>
给定一张待识别的人脸图片，在一个或多个人脸库中识别出最相似的前 N 个人脸。

4）人脸静态活体检测<br>
对用户上传的静态图片进行防翻拍活体检测，以判断是否是翻拍图片。

# **系统需求**
JDK >= 21 <br>
Maven >= 3.0 <br>
MySQL >= 8.0 <br>
Milvus >= 2.3

# **技术选型**
- 系统环境 <br>
Java EE 21 <br>
Servlet 6.0 <br>
Apache Maven 3

- 主框架 <br>
Spring Boot 3.2.x <br>
Spring Framework 6.0.x <br>

- 持久层 <br>
Spring JDBC

## **部署方式**
1. 下载模型文件（百度网盘：https://pan.baidu.com/share/init?surl=LlXe2-YsUxQMe-MLzhQ2Aw 提取码：ngne），将下载的所有*.csta模型文件放入某个路径下，在启动服务前，修改spring配置文件中的app.seetaface.model-path属性，指向存放模型文件夹的路径。
2. 修改spring配置文件中的face-image-path，用于存放人脸图片的文件夹。

## 在线体验
演示地址：[https://face.izerofx.com](https://face.izerofx.com/)
- 服务器配置较低，对体验有一定的影响。

## **演示图**
注：该工程不包含示例页面，以下示例页面UI、素材取自网络，仅用于演示使用。
<table>
    <tr>
        <td><img src="https://res.file.izerofx.com/face-service/1.png"/></td>
        <td><img src="https://res.file.izerofx.com/face-service/2.png"/></td>
    </tr>
    <tr>
        <td><img src="https://res.file.izerofx.com/face-service/3.png"/></td>
        <td><img src="https://res.file.izerofx.com/face-service/4.png"/></td>
    </tr>
</table>
