# Maximo 使用 REST API 调用 Automation Scripts 分页

## 创建Automation Scripts

首先在 Maximo 的 Automation Scripts 应用中创建一个自动化脚本，内容如下：

Script: GETWOS
Script Language: javascript
Allow Invoking Script Functions: true

``` javascript
load("nashorn:mozilla_compat.js");
importPackage(Packages.psdi.server);

var wos = [];
var page = request.getQueryParam("page");
var pageSize = request.getQueryParam("pageSize");

var woSet = MXServer.getMXServer().getMboSet("workorder",request.getUserInfo());
var woCount = woSet.count();
for(var i = 0; i < woCount; i++) {
    if (i < (page - 1) * pageSize) {
        continue;
    }
    if (i >= page * pageSize) {
        break;
    }
    var woRemote = woSet.getMbo(i);
    var wo = {};
    wo.wonum = woRemote.getString("WONUM");
    wo.workorderid = woRemote.getString("WORKORDERID");
    wo.description = woRemote.getString("DESCRIPTION");
    wo.status = woRemote.getString("STATUS");
    wo.location = woRemote.getString("LOCATION");
    wo.assetnum = woRemote.getString("ASSETNUM");
    wo.siteid = woRemote.getString("SITEID");
    wo.orgid = woRemote.getString("ORGID");
    wos.push(wo);
}

var responseBody = JSON.stringify(wos);
```

脚本里使用了两个请求参数 page 和 pageSize，所以需要再配置两个请求参数，在 Variables 页签下创建变量，配置如下：

``` shell
Variable: page
Variable Type: IN
Binding Type: LITERAL
Literal Data Type: ALN
Binding Value: page
```

``` shell
Variable: pageSize
Variable Type: IN
Binding Type: LITERAL
Literal Data Type: ALN
Binding Value: pageSize
```

## 测试

发送 REST 请求：

``` shell
/oslc/script/getwos?apikey={{api_key}}&lean=1&page=1&pageSize=10
```

返回结果如下：

``` javascript
[
  {
    "wonum": "7189",
    "workorderid": "163",
    "description": "Delivery",
    "status": "WAPPR",
    "location": "NEEDHAM",
    "assetnum": "",
    "siteid": "BEDFORD",
    "orgid": "EAGLENA"
  },
  。。。
]
```
