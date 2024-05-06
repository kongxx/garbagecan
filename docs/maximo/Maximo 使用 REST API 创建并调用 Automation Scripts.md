# Maximo 使用 REST API 创建并调用 Automation Scripts

## 创建 Automation Script

发送 POST 请求，参数如下:

``` shell
url: 
    POST /api/os/mxapiautoscript?apikey={{api_key}}&lean=1
headers:
    Content-Type: application/json
    properties: *
body:
    {
        "autoscript": "countofwoandsr",
        "scriptlanguage": "javascript",
        "loglevel": "ERROR",
        "source": "
    load('nashorn:mozilla_compat.js');
    importPackage(Packages.psdi.server);

    var resp = {};
    var site = request.getQueryParam('site');

    var woset = MXServer.getMXServer().getMboSet('workorder',request.getUserInfo());
    woset.setQbe('siteid','='+site);
    var woCount = woset.count();
    resp.wocount = woCount;

    var srset = MXServer.getMXServer().getMboSet('sr',request.getUserInfo());
    srset.setQbe('siteid','='+site);
    var srCount = srset.count();
    resp.srcount = srCount;
    resp.total = srCount+woCount;

    var responseBody = JSON.stringify(resp);
    "
    }
```

## 调用 Automation Script

发送 GET 请求，参数如下:

``` shell
url:
    GET /oslc/script/countofwoandsr?site=BEDFORD&apikey={{api_key}}&lean=1
headers:
    Content-Type: application/json
```

返回结果如下：

``` javascript
{
  "wocount": 16711,
  "srcount": 62,
  "total": 16773
}
```
