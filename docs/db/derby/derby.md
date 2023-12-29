# Derby

``` shell
$ wget -c https://archive.apache.org/dist/db/derby/db-derby-10.14.2.0/db-derby-10.14.2.0-bin.tar.gz
$ tar zxvf db-derby-10.14.2.0-bin.tar.gz
$ chmod -Rf 777 /opt/db-derby-10.14.2.0-bin
```

``` shell
$ cat /opt/db-derby-10.14.2.0-bin/derby.properties 
derby.connection.requireAuthentication=true
derby.authentication.provider=BUILTIN
#derby.user.<username>=<password>
derby.user.admin=Letmein
```

``` shell
$ cd /opt/db-derby-10.14.2.0-bin
$ bin/startNetworkServer -h 0.0.0.0 -p 1527
```
