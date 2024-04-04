# Oracle容器镜像制作

对于 Oracle 数据库的容器镜像制作，oracle 官方提供了 Dockerfile 文件和制作脚本的（https://github.com/oracle/docker-images）。这里以 12c 为例看看怎么使用。

1. 下载官方提供的 Dockerfile 文件和制作脚本

    ``` shell
    $ git clone https://github.com/oracle/docker-images.git
    ```

2. 准备数据库安装文件，并将文件放置在 docker-images/OracleDatabase/SingleInstance/{version} 目录下。这里我是将 linuxx64_12201_database.zip 或 V839960-01.zip 文件放置在了 12.2.0.1 目录下。(注：因为脚步里设置的安装包名为linuxx64_12201_database.zip ，所以如果使用V839960-01.zip文件，可以建个链接“ln -s V839960-01.zip linuxx64_12201_database.zip”)

3. 进入 docker-images/OracleDatabase/SingleInstance 目录，运行 buildContainerImage.sh 脚本

    ``` shell
    $ sudo ./buildContainerImage.sh -e -v 12.2.0.1
    ```

   其中 -e 表示安装企业版本，-v 指定安装的版本号。这一步运行时间比较长，需要耐心等待。安装完成后会创建一个容器镜像，大小大概有6G多。

    ``` shell
    $ sudo docker images
    REPOSITORY        TAG           IMAGE ID       CREATED        SIZE
    oracle/database   12.2.0.1-ee   0650dcb96360   12 hours ago   6.01GB
    oraclelinux       7-slim        970e50328c70   7 weeks ago    138MB
    ```

4. 启动容器实例

    适当根据自己需要修改端口端口、sid、密码等。
    
    ``` shell
    $ sudo docker run --name oracle12c \
      -p 1521:1521 -p 5500:5500 -p 2484:2484 \
      --ulimit nofile=1024:65536 --ulimit nproc=2047:16384 --ulimit stack=10485760:33554432 --ulimit memlock=3221225472 \
      -e ORACLE_SID=ORCLCDB \
      -e ORACLE_PDB=ORCLPDB1 \
      -e ORACLE_PWD=mypassword \
      -e ENABLE_ARCHIVELOG=true \
      -e ENABLE_TCPS=true \
      oracle/database:12.2.0.1-ee
    ```

5. 验证

    另起一个终端并仍然使用上面创建的容器来验证

    ``` shell
    sudo docker exec -it oracle12c sqlplus sys/mypassword as sysdba;

    或者

    sudo docker exec -it oracle12c sqlplus system/mypassword;
    ```

> 参考：https://github.com/oracle/docker-images

