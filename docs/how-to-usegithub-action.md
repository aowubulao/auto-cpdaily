## 利用Github Action进行签到

### 一、Fork并配置

1、进入仓库，点击右上角的**Fork**

![](https://img.neoniou.com/blog/20210207133537.png)

2、Fork过后自动进入了Fork过后的仓库

3、点击**Settings->Secrets**

![](https://img.neoniou.com/blog/20210207133709.png)

![](https://img.neoniou.com/blog/20210207133733.png)

4、点击**New Reposity secret**创建新的secret

![](https://img.neoniou.com/blog/20210207133849.png)

![](https://img.neoniou.com/blog/20210207133929.png)

5、分别创建**6个secret**，如图

![](https://img.neoniou.com/blog/20210207134319.png)

6、微信推送服务（可选）

本脚本使用server酱推送，首先你要申请到一个sc key，教程：[链接](https://blog.neoniou.com/posts/auto-serverless-readme/#6%E3%80%81%E9%85%8D%E7%BD%AE-Server%E9%85%B1%E6%8E%A8%E9%80%81)

随后将这个key也添加到secret中即可

![](https://img.neoniou.com/blog/20210207134530.png)

### 二、启动Github Actions

![](https://img.neoniou.com/blog/20210207134620.png)

1、点击Actions并启用

![](https://img.neoniou.com/blog/20210207134657.png)

![](https://img.neoniou.com/blog/20210207134718.png)

2、进行一次push

编辑README.md

![](https://img.neoniou.com/blog/20210207134906.png)



随便删除或者增加一个空格什么的就可以，滑到页面最下方

![](https://img.neoniou.com/blog/20210207135037.png)

### 三、完成！

进入Actions，可以看到已经成功启动，本脚本每天9:02，20:02，22:02会进行三次检测进行打卡

![](https://img.neoniou.com/blog/20210207135104.png)

### 四、如何进行更新？

删除掉这个仓库，重新进行[一]到[三]步即可