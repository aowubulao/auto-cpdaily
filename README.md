# 今日校园自动签到

### 使用前

本程序需要 **Java1.8** 的环境或更高，目前只适配西南大学的晨晚检签到系统
```shell
#检查自己的 java 配置
C:\Users\UserName> java -version

java version "1.8.0_181"
Java(TM) SE Runtime Environment (build 1.8.0_181-b13)
Java HotSpot(TM) 64-Bit Server VM (build 25.181-b13, mixed mode)
```

**闲暇的时候可以把服务一直挂在服务器或者电脑上，完成自动打卡功能**



### 下载

http://106.13.179.26/auto-cpdaily.zip

解压后目录：

```
|-auto-cpdaily.jar    	#主文件
|-daily.properties		#配置文件
|-start.cmd
```



### 配置

请先配置**daily.properties**配置文件

```properties
#学号
username=xxxxxxxxxxxxxxx

#密码，为身份证后六位
password=123456

#打卡位置，请使用 unicode 编码填写
#可以在这个网站转换编码 http://tool.chinaz.com/Tools/Unicode.aspx?qq-pf-to=pcqq.c2c
position=\u897f\u5357\u5927\u5b66

#打卡地址的经纬度，保留6位小数
#可以在这个网站查询打卡位置的经纬度 http://api.map.baidu.com/lbsapi/getpoint/
longitude=106.430691
latitude=29.826659
```



### 运行

##### Windows

直接双击start.cmd运行

##### Linux

```shell
nohup java -jar auto-cpdaily.jar &
```



### *开发

我没有把配置文件加入版本管理，如果要使用源码，请在resources目录下自行创建daily.properties文件