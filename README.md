# 今日校园自动签到-云函数版

### 1、使用前

本版本适合于没有服务器，或者不能长时间把服务挂在电脑上的人，不过部分设置比较麻烦，以及本人对云函数并不算太熟悉，可能稳定性已经更新的及时性没有普通版本的高。

不过此版本不需要在自己电脑上配置额外的环境，也算一个优点

如果你自己有服务器，请使用这个版本：https://github.com/aowubulao/auto-cpdaily/tree/master



### 2、下载

http://106.13.179.26/serverless.zip

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
#可以在这个网站转换编码 http://tool.chinaz.com/Tools/Unicode.aspx?qq-pf-to=pcqq.c2c
position=\u897f\u5357\u5927\u5b66

#打卡地址的经纬度，保留6位小数
#可以在这个网站查询打卡位置的经纬度 http://api.map.baidu.com/lbsapi/getpoint/
longitude=106.430691
latitude=29.826659
```



### 4、打包

将配置文件打入包中

此步骤建议下载 7zip 解压软件进行操作，地址：https://www.7-zip.org/download.html

**右键 auto-cpdaily-serverless.jar ---> 7-zip ---> 打开压缩包**

将 **daily.properties** 复制进压缩包中，然后关闭



### 5、云函数

#### 1）申请

搜索腾讯云，进入搜索云函数，申请即可，地址：https://console.cloud.tencent.com/scf/index?rid=1

#### 2）创建

申请完成后创建（新建函数）

**函数名称**：随便填一个

**运行环境**：选择Java8

**创建方式**：模板函数

然后点击下一步，此时有一个高级设置，展开高级设置，将**超时时间**设置为20s

点击**完成**即创建成功

#### 3）上传文件

打开刚才创建的函数，在**函数管理**的**函数代码**中

**提交方法**：本地上传Zip包

**执行方法**：daily.AutoDailyCp::mainHandler

点击上传，将刚才打包好的文件上传至网页，最后点击**保存**

#### 4）设置定时任务

触发管理中，创建触发器，可以自己设定定时多久执行一次，可以设置为一小时执行一次或者半小时执行一次。

最后**提交**即可。