
## Build Image
``` shell
sudo docker build -t kongxx/mycentos5:v1 .
```

## Run Container
``` shell
sudo docker run --name=test -it kongxx/mycentos5:v1 /bin/bash
```

## Use it in Docker
``` shell
FROM kongxx/mycentos5:v1

MAINTAINER Fanbin Kong "kongxx@hotmail.com"

EXPOSE 22
CMD ["/usr/sbin/sshd", "-D"]
```
