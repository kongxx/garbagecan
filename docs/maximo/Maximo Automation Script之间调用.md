# Maximo Automation Script之间调用

接前面几篇 Automation Script 文章，今天看看 Automation Script 之间怎么互相调用。

这里创建两个 Automation Script，第一个使用 Automation Script 查询数据库获取 workorder 列表，同时使用两个参数pageNum和pageSize来实现翻页，第二个 Automation Script 通过 service.invokeScript 调用第一个 Automation Script。

## 第一个Automation Script

Automation Script 代码如下（这里把脚本命名为 GETWOS)：

``` javascript
load('nashorn:mozilla_compat.js');
importPackage(Packages.psdi.server);
importPackage(Packages.psdi.security);
importPackage(Packages.java.sql);

// var pageNum = request.getQueryParam('pageNum');
// var pageSize = request.getQueryParam('pageSize');

var mxServer = MXServer.getMXServer();
var connKey = mxServer.getSystemUserInfo().getConnectionKey();
var conn = mxServer.getDBManager().getConnection(connKey);

var wos = [];

var stmt = conn.createStatement();
var rs = stmt.executeQuery('select * from workorder order by WORKORDERID');
var i = 0;
while(rs.next()) {
    var wo = {};
    if (i < (pageNum - 1) * pageSize) {
        continue;
    }
    if (i >= pageNum * pageSize) {
        break;
    }
    i++;
    wo.wonum = rs.getString('WONUM');
    wo.workorderid = rs.getString('WORKORDERID');
	wo.description = rs.getString('DESCRIPTION');
    wos.push(wo);
}

rs.close()
stmt.close()
conn.close()
var response = JSON.stringify(wos);
```

其中：
1. pageNum 和 pageSize 不用声明，并且也不需要通过 request 对象的方法来获取，因为是通过另外一个 Automation Script 来调用的，在另一个 Automation Script 中会通过上下文来传递这两个参数，这里直接使用就可以了。
2. 这个 Automation Script 最后会返回一个json。

## 第二个Automation Script

Automation Script 代码如下：

``` javascript
load('nashorn:mozilla_compat.js');
importPackage(Packages.psdi.server);
importPackage(Packages.psdi.security);
importPackage(Packages.java.util);

var ctx = new HashMap();
ctx.put("pageNum", "1");
ctx.put("pageSize", "10");
service.invokeScript("GETWOS", ctx);
var jsonResp = ctx.get("response");
var responseBody = jsonResp;
```

其中：
1. 使用一个Map来保存传递给被调用 Automation Script 的参数，这里就是pageNum和pageSize；
2. 使用service.invokeScript(scriptName, context) 调用其它 Automation Script；
3. 最后从context中获取执行结果；

## 验证

``` shell
curl --request GET --url '<base_url>/maximo/api/script/getwos3?apikey=<api_key>&lean=1' --header 'content-type: application/json' --header 'user-agent: vscode-restclient'
```

## 参考
- https://bportaluri.com/2019/12/service-methods-automation-scripts.html
- https://bportaluri.com/2019/12/maximo-scripting-library-scripts.html
- https://bportaluri.com/wp-content/MaximoJavaDocs76/com/ibm/tivoli/maximo/script/ScriptService.html
