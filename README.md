# 简读RSS
使用Google Data Binding技术以MVVM模式来实现的一款Material Design风格的RSS阅读器。
##功能点
* 用户模块:（使用Bmob、greenDao实现）用户信息的同步、登录、注册等操作。 
* 频道模块:（RecyclerView）频道的下拉刷新、上拉加载、长按操作。 
* 文章模块:文章列表的刷新(RSS解析)、删除、详情的查看（RichTextView）、延伸的阅读(WebView)、分享(ShareSDK)等。 
* 订阅模块:增加自定义的订阅(RSS)、系统推荐的频道展示。 
* 系统设置:清理本地的存储空间、阅读方式等。

##Apk Download  

 
[点击下载apk](/apk/app-easy-read.apk)

##效果图
 ![image](https://github.com/moonChenHaohui/blog/blob/gh-pages/image/start.gif)
 ![image](https://github.com/moonChenHaohui/blog/blob/gh-pages/image/login.gif)
 ![image](https://github.com/moonChenHaohui/blog/blob/gh-pages/image/back.gif)
 ![image](https://github.com/moonChenHaohui/blog/blob/gh-pages/image/set.gif)

##依赖
* 数据绑定==>Android Data Binding，1.0-rc4 : [data binding home page](http://developer.android.com/tools/data-binding/guide.html)
* android.support==>design:23.1.1、recyclerview-v7:23.1.1、cardview-v7:23.1.1
* ORM框架==>GreenDao，V2.0.0
* 后端支撑==>Bmob Sdk，V3.4.5：[http://www.bmob.cn](http://www.bmob.cn)
* RSS解析==>Rome.Jar，V1.0.0
* Json解析==>Gson,V2.5
* 事件总线==>TinyBus，V3.0.2 ：[https://github.com/beworker/tinybus](https://github.com/beworker/tinybus)
* 图片加载==>Fresco，+ ： [http://www.fresco-cn.org](http://www.fresco-cn.org)
* UI组件-MD开源组件==>materialdialog、materialedittext
* UI组件-字体图标==>iconfy, V2.1.0 ：[https://github.com/JoanZapata/android-iconify](https://github.com/JoanZapata/android-iconify)
* HTTP通信框架==>Volley，V1.0.19 
* 微信接入SDK



##Log
Click here [Log](https://github.com/moonChenHaohui/easy-Read-for-RSS/blob/master/LOG.md "Log") .

## 欢迎联系我
Moon，Java程序猿，moon4chen@163.com
>初入Android坑，求大神带飞；