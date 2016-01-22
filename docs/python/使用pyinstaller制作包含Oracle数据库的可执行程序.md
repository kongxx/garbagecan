# 使用pyinstaller制作包含Oracle数据库的可执行程序

## 准备
首先参考[Linux上pip安装cx_Oracle]安装cx_Oracle库和oracle驱动instantclient_11_2。
这里假定oracle驱动还是解压到/opt/instantclient_11_2目录下，内容大致如下：
``` bash
/opt/instantclient_11_2
├── adrci
├── BASIC_LITE_README
├── genezi
├── libclntsh.so.11.1
├── libnnz11.so
├── libocci.so.11.1
├── libociicus.so
├── libocijdbc11.so
├── ojdbc5.jar
├── ojdbc6.jar
├── sdk
├── uidrvci
└── xstreams.jar
```

## 测试程序

创建一个目录myapp，在其下新建一个test.py文件，内容如下：

``` python
import cx_Oracle

conn = cx_Oracle.connect('jhinno/jhinno@192.168.0.188/jhinno')  
cursor = conn.cursor ()
cursor.execute ("select sysdate from dual")
row = cursor.fetchone ()
print row

cursor.close ()
conn.close ()
```

## 打包spec文件
在myapp目录下创建test.spec文件，内容如下：
``` python
# -*- mode: python -*-
a = Analysis(['test.py'],
             pathex=['myapp'],
             hiddenimports=[],
             hookspath=None,
             runtime_hooks=None)
a.binaries = a.binaries + [('libclntsh.so', '/opt/instantclient_11_2/libclntsh.so.11.1','BINARY')]
a.binaries = a.binaries + [('libnnz11.so', '/opt/instantclient_11_2/libnnz11.so','BINARY')]
a.binaries = a.binaries + [('libocci.so', '/opt/instantclient_11_2/libocci.so.11.1','BINARY')]
a.binaries = a.binaries + [('libociicus.so', '/opt/instantclient_11_2/libociicus.so','BINARY')]
pyz = PYZ(a.pure)
exe = EXE(pyz,
          a.scripts,
          a.binaries,
          a.zipfiles,
          a.datas,
          name='test',
          debug=False,
          strip=None,
          upx=True,
          console=True )
```
> 注意其中a.binaries的配置，将oracle需要用到的库加了进来。

## 打包
运行pyinstaller命令，如下：
``` bash
pyinstaller test.spec
```

打包后在myapp/dist目录下生成test的可执行文件，可以直接运行来测试。
