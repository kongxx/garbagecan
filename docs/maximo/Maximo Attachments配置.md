# Maximo Attachments配置

以下内容以 Windows 上 Maximo 为例，并假定设置 DOCLINKS 的根路径为 "C:\DOCLINKS"。

## HTTP Server配置

1. 修改C:\Program Files\IBM\HTTPServer\conf\httpd.conf文件

2. 查找 "DocumentRoot" 并修改成如下配置

``` shell
DocumentRoot "C:\DOCLINKS"
```

3. 查找 “<Directory "。。。">” 并修改成和上一步 DocumentRoot 同样的值

``` shell
<Directory "C:\DOCLINKS">
```

4. 重启 http 服务

5. 验证

在 "C:\DOCLINKS" 目录下创建一个html文件，比如test.html，然后通过浏览器访问 "http://<host>/test.html"

## Maximo配置

1. 登录 Maximo，访问 System Configuration -> Platform Configuration -> System Properties

2. 查找所有包含 "path" 的属性，修改如下几个值 (Global Value)

``` shell
mxe.doclink.doctypes.defpath = C:\DOCLINKS (?)
mxe.doclink.doctypes.topLevelPaths = C:\DOCLINKS
mxe.doclink.path01 = C:\DOCLINKS = http://<host name/ip>
```

3. 保存并执行“Live Refresh”

4. 重新启动 maximo 应用。

## 验证

1. 访问 Work Orders -> Work Order Tracking

2. 选择一个workorder，在详细页的 attachments 链接处上传附件。

3. 在详细页的 attachments 链接里打开新上传的附件。

## 参考

- https://www.ibm.com/support/pages/configuring-doclinks-mxedoclinksecuredattachment-set-true

- https://www.ibm.com/support/pages/configuring-doclinks-mxedoclinksecuredattachment-set-true
