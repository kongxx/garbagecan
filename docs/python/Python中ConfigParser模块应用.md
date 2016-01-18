 
# Python中ConfigParser模块应用

Python的ConfigParser模块定义了3个对INI文件进行操作的类 RawConfigParser，ConfigParser和SafeConfigParser。其中RawCnfigParser是最基础的INI文件读取类，ConfigParser、SafeConfigParser支持对%(value)s变量的解析。 

下面看看怎样通过ConfigParser类来解析一个ini文件。

配置文件settings.cfg
``` ini
[DEFAULT]
mykey=myvalue

[section_a]
key1=value1
key2=value2
key3=value3

[section_b]
key1=value1
key2=value2
key3=value3

[db]
db_host=127.0.0.1
db_port=5432
db_user=admin
db_pass=Letmein
db_name=test
db_url = jdbc:postgresql://%(db_host)s:%(db_port)s/%(db_name)s
```

测试代码如下
``` python
#coding:utf-8
import ConfigParser, os

# 读取配置文件
cp = ConfigParser.ConfigParser()
cp.read(['settings.cfg'])

#  获取所有defaults section
defaults = cp.defaults()
print defaults

#  获取所有sections
sections = cp.sections()
print sections

# 判断指定 section 是否存在
has_db = cp.has_section('db')
print has_db

# 获取指定 section 的配置信息，只获取键
options = cp.options('db')
print options

# 获取指定 section 的配置信息，获取键值
items = cp.items('db')
print items

# 获取指定 section 的配置信息，根据键获取值
db_host=cp.get('db', 'db_host')
db_port=cp.getint('db', 'db_port')
db_user=cp.get('db', 'db_user')
db_pass=cp.get('db', 'db_pass')
db_name=cp.get('db', 'db_name')
db_url=cp.get('db', 'db_url')
print db_host, db_port, db_name, db_user, db_pass

# 使用 DEFAULT section 值
myvalue=cp.get('db', 'mykey')
print myvalue

# 添加一个section
cp.add_section('section_c')
cp.set('section_c', 'key1', 'value111')
cp.set('section_c', 'key2', 'value222')
cp.set('section_c', 'key3', 'value333') 
cp.set('section_c', 'key4', 'value444') 
cp.write(open("test1.cfg", "w"))

# 移除 option
cp.remove_option('section_c','key4') 

# 移除 section
cp.remove_section('section_c') 

cp.write(open("test2.cfg", "w"))
```