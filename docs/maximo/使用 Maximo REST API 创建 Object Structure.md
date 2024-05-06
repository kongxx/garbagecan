# 使用 Maximo REST API 创建 Object Structure

接前面的文章，今天通过编写Python脚本的方式使用 Maximo REST API 创建Object Structure。

## 创建 object structure

这里创建一个新的 Work Order Object Structure，命名为 MXAPIWO123。

``` python
import requests

url = "<maximo url>/api/os/mxintobject"

querystring = {
    "apikey":"<api key>",
    "lean":"1"
}

payload = """{
    "intobjectname": "MXAPIWO123",
    "description": "Maximo API for Work Orders",
    "usewith": "INTEGRATION",
    "module": "WO",
    "authapp": "MXAPIWO",
    "maxintobjdetail": [{
            "objectname": "WORKORDER",
            "hierarchypath": "WORKORDER",
            "objectorder": 1
        },{
            "objectname": "WORKLOG",
            "hierarchypath": "WORKORDER/WORKLOG",
            "parentobjname": "WORKORDER",
            "relation": "MODIFYWORKLOG",
            "objectorder": 1
            }
        ]
    }"""

headers = {
    'user-agent': "vscode-restclient",
    'content-type': "application/json",
    'properties': "*"
}

response = requests.request("POST", url, data=payload, headers=headers, params=querystring)

print(response.text)
```

运行脚本

``` shell
python create.py | json_pp
```

## 查看 object structure

``` python
import requests

url = "<maximo url>/api/os/mxintobject"

querystring = {
    "apikey": "<api key>",
    "lean": "1",
    "oslc.where": "intobjectname=\"MXAPIWO123\"",
    "oslc.select": "*" 
}

headers = {
    'user-agent': "vscode-restclient",
    'content-type': "application/json"
}

response = requests.request("GET", url, headers=headers, params=querystring)

print(response.text)
```

运行脚本

``` shell
python info.py | json_pp
```

## 使用 object structure

这里使用上面创建的 MXAPIWO123 Object Structure 查询 Work Order 数据。

``` python
import requests

url = "<maximo url>/api/os/MXAPIWO123"

querystring = {
    "apikey": "<api key>",
    "lean": "1",
    "oslc.pageSize": 10,
    "oslc.select": "*" 
}

headers = {
    'user-agent': "vscode-restclient",
    'content-type': "application/json"
}

response = requests.request("GET", url, headers=headers, params=querystring)

print(response.text)
```

运行脚本

``` shell
python query.py | json_pp
```
