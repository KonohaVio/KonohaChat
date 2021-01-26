# Konoha Bot Service

English: Just a bot service for fun. Based on Mirai.

简体中文: 一个基于Mirai的QQ chat bot。

功能列表：

<当前功能列表>
[模块：人工智障聊天模式]

蕾姆陪我聊天
蕾姆来聊天
结束人工智障模式：/退下吧

[模块：无情复读姬]

[模块：投票禁言]
禁言@xxx

[模块：精致睡眠]
我要精致睡眠

[模块：禁言套餐]
我要休息x分钟，我要休息x小时

[模块：进群欢迎]

[模块：百度百科]
/百科+空格+查询词

[模块：涩图]
/涩图+空格

[模块：百合涩图]

/百合涩图
[模块：P站搜图]

/搜图+空格
[模块：一言]
/一言

[模块：B站链接解析]

[模块：能不能好好说话(缩写查询)]
/sx+空格+缩写


###### 前提：**按照Miari的基础教程，成功实现了Kotlin语言，gradle项目的第一个test，会较好的理解这些代码**

代码结构：分data，function，util，和主项四部分。
function是各项功能，有些需要依赖data，util的类。
主项有三个，Main，messageDSL，QuestionAnswer。分别装载了多种功能。
Main有登录，初始化，群管理的一些功能。
messageDSL有一言，B站链接解析，缩写查询，colorphoto等
QuestionAnswer有图灵机器人等
这是因为各项功能的实现方式不一，所以装载方式也不一（四处找的

依赖：build.gradle文件里写了很多依赖，可能要下挺久的。
另外，目录下的build文件夹下的；libs文件夹里也有一些包，都是依赖包

启动前的工作：到com.mrwhoami.qqservices.function的BotInitInfo这个文件里填入qqID和password
QuestionAnswer文件的第24行，也有需要填qq群号的（别问为何没有独立的config文件，问就是菜

启动方式：调试代码时：idea正常运行就行。
要长时间挂着bot时，一般是放某个盘，然后利用cmd命令行，到代码所在目录，运行gradle run
或者gradle release打包成jar包，再java -jar xxx.jar运行。
两种方式都在挂着bot的时候占用较小内存，个人常用第一种，因为可以继续debug。
每次都cmd很麻烦？那就写个dos脚本吧，目录下的Run.cmd就是一个例子，可以重命名为.txt文件查看，修改即可。

说到脚本，Himage文件夹里也有个脚本，rename.cmd，是批量重命名jpg文件。也可以修改里面的代码，释放其更强大的功能（重命名任意后缀名的文件）



