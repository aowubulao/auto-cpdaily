# 今日校园自动签到-云函数版

### 0、版本

此脚本分为两个版本

现在浏览的版本是云函数版：云函数版可以通过将文件上传到腾讯云函数，定时执行，不需要有服务器，也是基本免费的。

另一个是普通版：[链接](https://github.com/aowubulao/auto-cpdaily/tree/master)，普通版需要程序一直在电脑的后台运行，开箱即用，适合有服务器的人使用。



**7.23晚**:修复了同一设备签到的问题



### 1、使用前

**有任何问题可以联系我：me@neow.cc**

**或者在此页面提issue**

目前只适用于西南大学晨晚检签到系统

文档图片打不开的可以看这：[链接](https://blog.neoniou.com/posts/auto-serverless-readme/)

------



### 2、下载

[下载链接1](http://106.13.179.26/serverless.zip)

解压后目录：

```
|-auto-cpdaily-serverless.jar	#主程序
|-daily.properties				#配置文件
```



### 3、配置

配置方法同普通版本，右键记事本打开 **daily.properties** 即可

```properties
#学号
username=xxxxxxxxxxxxxxx

#密码，为身份证后六位
password=123456

#打卡位置，请使用 unicode 编码填写
#西南大学转换为 unicode编码 为 ：\u897f\u5357\u5927\u5b66
#改为你自己平时定位的地址名字即可，为中文地址
#可以在这个网站转换编码 http://tool.chinaz.com/Tools/Unicode.aspx?qq-pf-to=pcqq.c2c
position=\u897f\u5357\u5927\u5b66

#打卡地址的经纬度，保留6位小数
#可以在这个网站查询打卡位置的经纬度 http://api.map.baidu.com/lbsapi/getpoint/
longitude=106.430691
latitude=29.826659
```



### 4、打包

将配置文件打入包中

此步骤建议下载 7zip 解压软件(测试360压缩也可以)进行操作

地址：https://www.7-zip.org/download.html

**右键 auto-cpdaily-serverless.jar ---> 7-zip ---> 打开压缩包**

用其他压缩软件应该也可以，右键用压缩软件打开即可，**不要解压**

将 **daily.properties** 复制进压缩包中，然后关闭



### 5、云函数

#### 1）申请

搜索腾讯云，进入搜索云函数，申请即可，地址：https://console.cloud.tencent.com/scf/index?rid=1

#### 2）创建

申请完成后创建（新建函数）

**函数名称**：随便填一个

**运行环境**：选择Java8

**创建方式**：模板函数



<img src="https://img.neoniou.com/readme/auto-serverless-1.png" style="zoom:80%;" />



然后点击下一步，此时有一个高级设置，展开高级设置，将**超时时间**设置为20s

内存建议设置为256MB或更高



<img src="https://img.neoniou.com/readme/auto-serverless-2.png" style="zoom: 80%;" />



点击**完成**即创建成功

#### 3）上传文件

打开刚才创建的函数，在**函数管理**的**函数代码**中

**提交方法**：本地上传Zip包

**执行方法**：daily.AutoDailyCp::mainHandler

点击上传，将刚才打包好的文件上传至网页，最后点击**保存**



<img src="https://img.neoniou.com/readme/auto-serverless-3.png" style="zoom: 80%;" />



#### 4）设置定时任务

触发管理中，创建触发器，可以自己设定定时多久执行一次，可以设置为半小时执行一次或者一小时执行一次。

最后**提交**即可。



<img src="https://img.neoniou.com/readme/auto-serverless-4.png" style="zoom: 80%;" />

#### 5）日志查询

可以在日志查询处查询运行状态和运行日志。