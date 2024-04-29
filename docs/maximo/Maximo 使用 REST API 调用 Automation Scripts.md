# Maximo 使用 REST API 调用 Automation Scripts

## 创建Automation Scripts

首先在 Maximo 的 Automation Scripts 应用中创建一个自动化脚本，内容如下：

Script: countofwoandsr
Script Language: javascript
Allow Invoking Script Functions: true

``` javascript
load("nashorn:mozilla_compat.js");
importPackage(Packages.psdi.server);

var resp = {};
var site = request.getQueryParam("site");

var woset = MXServer.getMXServer().getMboSet("workorder",request.getUserInfo());
woset.setQbe("siteid","="+site);
var woCount = woset.count();
resp.wocount = woCount;

var srset = MXServer.getMXServer().getMboSet("sr",request.getUserInfo());
srset.setQbe("siteid","="+site);
var srCount = srset.count();
resp.srcount = srCount;
resp.total = srCount+woCount;

var responseBody = JSON.stringify(resp);
```

脚本里使用了一个请求参数 site，所以需要再配置一个请求参数，在 Variables 页签下创建变量，配置如下：

``` shell
Variable: site
Variable Type: IN
Binding Type: LITERAL
Literal Data Type: ALN
Binding Value: site
```

## 测试

发送 REST 请求：

``` shell
/oslc/script/countofwoandsr?site=BEDFORD&apikey={{api_key}}&lean=1
```

返回结果如下：

``` javascript
{
  "wocount": 16711,
  "srcount": 62,
  "total": 16773
}
```
