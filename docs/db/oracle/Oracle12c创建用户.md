# Oracle12c创建用户

首先以Oracle管理员用户登录系统，并使用sys用户登录Oracle
``` shell
$ sudo su - oracle
$ sqlplus /nosql
SQL> conn sys as sysdba;
Enter password: 
Connected.
```

## 创建临时表空间

``` shell
# 查看临时表空间
SQL> select name from v$tempfile;

# 创建临时表空间
SQL> create temporary tablespace test_temp tempfile '/home/oracle/app/oracle/oradata/orcl/test_temp.dbf' size 1000M autoextend on next 100M maxsize unlimited;
```

## 创建数据表空间

``` shell
# 查看数据表空间
SQL> select name from v$datafile;

# 创建数据表空间
SQL> create tablespace test_data datafile '/home/oracle/app/oracle/oradata/orcl/test_data.dbf' size 1000M autoextend on next 500M maxsize unlimited;
```

## 创建用户

``` shell
SQL> create user test identified by test default tablespace test_data temporary tablespace test_temp;

create user test identified by flintstone;
ORA-65096: invalid common user or role name

# Oracle 12c 执行上面的命令会出错，需要先执行下面的语句，然后再创建用户

SQL> alter session set "_ORACLE_SCRIPT"=true;

# 创建用户
SQL> create user test identified by test default tablespace test_data temporary tablespace test_temp;

User created.
```

## 赋予权限

``` sql
SQL> GRANT ALL PRIVILEGES TO test;

SQL> GRANT DBA TO test;
```
