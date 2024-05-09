# Maximo 在 Automation Script 中访问数据库

在 Automation Script 中我们通常使用 mbo 对象来操作数据，但有时候当数据量较大时，使用 mbo 对象来操作数据会比较慢。这时候，我们可以使用 JDBC 的方式来直接访问数据库，从而提高操作数据的效率。

下面看看使用 JavaScript 脚本怎么实现在 Automation Script 中访问数据库：

1. 首先需要倒入必要的包

``` javascript
load('nashorn:mozilla_compat.js');
importPackage(Packages.psdi.server);
importPackage(Packages.psdi.security);
importPackage(Packages.psdi.security);
importPackage(Packages.java.sql);
```

2. 获取 Connection 实例

``` javascript
var mxServer = MXServer.getMXServer();
var connKey = mxServer.getSystemUserInfo().getConnectionKey();
var conn = mxServer.getDBManager().getConnection(connKey);
```

3. 执行 SQL 语句并处理结果

``` javascript
var wos = [];

var stmt = conn.createStatement();
var rs = stmt.executeQuery("select * from workorder order by WORKORDERID");
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
    wo.wonum = rs.getString("WONUM");
    wo.workorderid = rs.getString("WORKORDERID");
	wo.description = rs.getString("DESCRIPTION");
    wos.push(wo);
}

rs.close()
stmt.close()
conn.close()
```

4. 以 json 格式返回

``` javascript
var responseBody = JSON.stringify(wos);
```

## 完整代码

下面看一下完整的 Automation Script 代码：

``` javascript
load('nashorn:mozilla_compat.js');
importPackage(Packages.psdi.server);
importPackage(Packages.psdi.security);
importPackage(Packages.psdi.security);
importPackage(Packages.java.sql);

var pageNum = request.getQueryParam("pageNum");
var pageSize = request.getQueryParam("pageSize");

var mxServer = MXServer.getMXServer();
var connKey = mxServer.getSystemUserInfo().getConnectionKey();
var conn = mxServer.getDBManager().getConnection(connKey);

var wos = [];

var stmt = conn.createStatement();
var rs = stmt.executeQuery("select * from workorder order by WORKORDERID");
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
    wo.wonum = rs.getString("WONUM");
    wo.workorderid = rs.getString("WORKORDERID");
	wo.description = rs.getString("DESCRIPTION");
    wos.push(wo);
}

rs.close()
stmt.close()
conn.close()

var responseBody = JSON.stringify(wos);
```

## 测试

使用 vscode 的 REST Client 插件来测试这个 Automation Script

``` shell
### 
# call automation script
GET {{base_url}}/api/script/<automation script>
    ?apikey={{api_key}}
    &lean=1
    &pageNum=1
    &pageSize=10
Content-Type: application/json
```

返回结果类似如下：

``` json
[
  {
    "wonum": "1638",
    "workorderid": "1",
    "description": "Check Leaking Condensate Return Pump"
  },
  ...
]
```
