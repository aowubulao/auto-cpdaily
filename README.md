# 今日校园自动签到

### 0、版本

此脚本分为两个版本，一个是普通版：[链接](https://github.com/aowubulao/auto-cpdaily/tree/master)，普通版需要程序一直在电脑的后台运行，开箱即用，适合有服务器的人使用。

另一个版本是云函数版：[链接](https://github.com/aowubulao/auto-cpdaily/tree/serverless)，云函数版可以通过将文件上传到腾讯云函数，定时执行，不需要有服务器，也是基本免费的。



### 1、使用前

本程序需要 **Java1.8** 的环境或更高，目前只适配西南大学的晨晚检签到系统
```shell
#检查自己的 java 配置
C:\Users\UserName> java -version

java version "1.8.0_181"
Java(TM) SE Runtime Environment (build 1.8.0_181-b13)
Java HotSpot(TM) 64-Bit Server VM (build 25.181-b13, mixed mode)
```



### 2、下载

[下载链接1](http://106.13.179.26/auto-cpdaily.zip)

[下载链接2](https://github.com/aowubulao/auto-cpdaily/releases/) （common.zip）

解压后目录：

```
|-auto-cpdaily.jar    	#主文件
|-daily.properties		#配置文件
|-start.cmd
```



### 3、配置

请先配置**daily.properties**配置文件，可以右键记事本打开

```properties
#学号
username=xxxxxxxxxxxxxxx

#密码，为身份证后六位
password=123456

#打卡位置，请使用 unicode 编码填写
#西南大学 转换为 unicode编码 为 ：\u897f\u5357\u5927\u5b66
#改为你自己平时定位的地址名字即可，为中文地址
#可以在这个网站转换编码 http://tool.chinaz.com/Tools/Unicode.aspx?qq-pf-to=pcqq.c2c
position=\u897f\u5357\u5927\u5b66

#打卡地址的经纬度，保留6位小数
#可以在这个网站查询打卡位置的经纬度 http://api.map.baidu.com/lbsapi/getpoint/
longitude=106.430691
latitude=29.826659
```



### 4、运行

##### Windows

直接双击start.cmd运行

##### Linux

```shell
nohup java -jar auto-cpdaily.jar &
```



### *开发

我没有把配置文件加入版本管理，如果要使用源码，请在resources目录下自行创建daily.properties文件