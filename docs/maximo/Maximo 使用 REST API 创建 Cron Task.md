# Maximo 使用 REST API 创建 Cron Task

接前面几篇文章，我没有了 automation script 以后，有时候需要让其定期自动执行，这时候就可以通过 Cron Task 来实现了。

## 通过Maximo REST API 来创建 Cron Task
request：
``` shell
POST {{base_url}}/api/os/mxapicrontaskdef?apikey={{api_key}}&lean=1
```

headers：
``` shell
Content-Type: application/json
properties: *
```

body：
``` shell
{
    "crontaskname": "mycrontask",
    "description": "my cron task definition",
    "classname": "com.ibm.tivoli.maximo.script.ScriptCrontask",
    "accesslevel": "FULL", 
    "crontaskinstance": [
        {
            "instancename": "<automation script name>",
            "schedule": "1m,*,*,*,*,*,*,*,*,*",
            "active": true,
            "runasuserid": "MAXADMIN",
            "keephistory": true,
            "maxhistory": 100,
            "crontaskparam": [
                {
                    "parameter": "SCRIPTARG",
                    "value": ""
                }, {
                    "parameter": "SCRIPTNAME",
                    "value": "<automation script name>"
                }
            ]
        }
    ]
}
```
其中 <automation script name> 是我们创建的 automation script 实例，并且设置每分钟运行一次。

## 通过maximo ui创建

1. 访问 System Configuration -> Platform Configuration -> Cron Task Setup 
2. 左侧导航上选择"New Cron Task Definition"
3. 设置Cron Task属性：
Cron Task: myCronTask
Class: com.ibm.tivoli.maximo.script.ScriptCrontask
Access Level: FULL
4. 设置Cron Task Instances
Cron Task Instance Name: <automation script name>
Schedule: 1m,*,*,*,*,*,*,*,*,*
Run as User： MAXADMIN
添加一个参数 SCRIPTNAME: <automation script name>
5. 保存

## 验证
通过maximo ui参考cron task instance的History，可以看到定期执行历史。
