# **简介**
基于中科视拓开源人脸检测技术模型，通过利用Java技术和向量搜索技术提供包括人脸检测与分析、比对、搜索、验证、五官定位、活体检测等API接口服务功能，为开发者和企业提供高性能高可用的人脸识别服务。可应用于在线娱乐、在线身份认证等多种应用场景，充分满足各行业客户的人脸属性识别及用户身份确认等需求。
<br><br>
关于中科视拓开源人脸模型（SeetaFace6），您可以访问[https://github.com/SeetaFace6Open/index](https://github.com/SeetaFace6Open/index)了解更多。
<br>
SeetaFace6入门教程[https://github.com/seetafaceengine/SeetaFaceTutorial](https://github.com/seetafaceengine/SeetaFaceTutorial)了解更多。
# **技术架构**
基于Spring Boot+MySQL+Milvus的技术组合，接口采用RESTful API定义，通过JNI（Java Native Interface）技术调用底层的人脸识别模型，实现人脸的检测与识别。

# **接口范围**
本API服务主要专注于提供包括人脸检测与分析、比对、搜索、验证、五官定位、活体检测等API接口服务，在该API服务中实现人脸检测与分析、五官定位、人脸比对、人脸库管理、人脸搜索、人脸静态活体检测，主要包括如下API对外接口：
1）人脸检测与分析：
对请求图片检测返回人脸的位置、面部属性；对请求图片进行五官定位，计算构成人脸轮廓的68个点
2）人脸比对：
对两张图片中的人脸进行相似度比对，返回人脸相似度分数。
3）人脸库管理：
包括增、删人脸库，人脸库增、删人脸。
3）人脸搜索
给定一张待识别的人脸图片，在一个或多个人脸库中识别出最相似的前 N 个人脸。
4）人脸静态活体检测
对用户上传的静态图片进行防翻拍活体检测，以判断是否是翻拍图片。

# **系统需求**
JDK >= 21 <br>
Maven >= 3.0 <br>
MySQL >= 8.0 <br>
Milvus >= 2.3

# **技术选型**
1、系统环境 <br>
Java EE 21 <br>
Servlet 6.0 <br>
Apache Maven 3

2、主框架 <br>
Spring Boot 3.2.x <br>
Spring Framework 6.0.x <br>

3、持久层 <br>
Spring JDBC