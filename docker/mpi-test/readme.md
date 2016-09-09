
# Build Image
``` bash
sudo docker build -t mpi-test:v1 .
```

# Run Container
``` bash
sudo docker run --name=mpitest1 -h mpitest1 -v `pwd`/apps:/apps --net='none' -d -P mpi-test:v1
sudo ./bind_addr.sh mpitest1 172.17.0.211

sudo docker run --name=mpitest2 -h mpitest2 -v `pwd`/apps:/apps --net='none' -d -P mpi-test:v1
sudo ./bind_addr.sh mpitest2 172.17.0.212
```

``` bash
sudo docker stop mpitest1 mpitest2
sudo docker rm mpitest1 mpitest2
```
