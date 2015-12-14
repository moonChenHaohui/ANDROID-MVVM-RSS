# easy-Read-for-RSS

android毕业设计项目－一款清爽的rss阅读器

>开始日期：2015/10/19

>大四程序猿，初入Android坑，求大神带飞。
Email:moon4chen@163.com


##使用组件
* tinyBus
* fresco
* iconify
* volley
* greendao
* Bmob
* material Design Libs



##进度
TODO:

1. listview -> recyclerView;
2. settingActivity
3. >rss获取原理研究
4. >本地sqlite保存＋shareprefrence保存
5. >rss网络请求队列部分是否可以参考okhttp queue部分
6. >greenDao研究(10.08提出)
7. 动画框架 ：NineOldAndroids
8. 后台：Bmob
9. volley ->离线缓存机制的实现

###Log

12.14
- 增加了 顶部toast
- add 刷新更新提示

12.13
- 增加了 feed 刷新 文章的功能

12.10-12

- none

12.09

- 配置Bmob
- 增加feed list  menu 操作

12.08

- none

12.06

- 增加recyclerview 长按操作以及弹出menu
- greendao生存文件丢失，重新生成数据库，并添加一些字段

12.05

- 修改主页样式
- 修正富文本 文字链接到自带的webview中
- 修正图片浏览功能bug

12.04

- 依据article图片数量，分成3种展现形式的recyclerview item

11.29-12.03

- 回家

11.28

- 增加图片浏览

11.27

- 富文本 改使用volley加载图片

11.16-11.26

- none －大概是去玩游戏了吧。。。

11.15

- 增加article富文本使用异步线程去加载图片

11.14

- 增加article页面的逻辑
- 增加本地数据库的操作

11.13

- none

11.12

- 使用rome进行rss解析

11.11

- none

11.10

- 移除部分垃圾代码、修改部分错误的命名
- 修复recyclerview 顶部notify无效的bug

11.09

- ui调整
- 修复databinding onclick无效的bug
- 本地数据库生成 greendao
- 使用本地模拟user进行登录操作

11.08

- 增加 登录 activity
- 搭建php 后台，使用volley测试user post method
- 增加volley操作helper

11.07

- 增加 empty in recyclerview
- 添加recyclerview上拉刷新、下拉加载功能

11.06

- none

11.05

- 增加了平滑滚动的scrollview

11.01-11.04

- none

10.30

- 进行rss解析的尝试，使用了线程进行请求

10.29

- rss解析部分：使用rome.jar + sax 解析；写出demo和参数部分
- 添加了greenDao

10.26-10.28
  休息

10.25

- 了解prefrenceFrameLayout
- add setting page;
- 休息

10.24

- 添加了feed页面
- 添加了平滑滚动scroll
- 添加了feed web页面

10.23

- 添加了分组的feeds listview；
- 平滑滚动的研究

10.22

-  整理git资源
- 增加material dialog、edittext；
- 增加添加url订阅的dialog；
- 添加了左滑返回，修改了滑动区域以及root判断；

10/21
使用databing改写了现有的drawer、main部分；美化了drawer部分ui

10/20
放松

10/19
了解databinding、搭建drawer部分,loadtoast.，了解了toolbar、drawerlayout