
# Build Image

``` bash
sudo docker build -t license-env:v1 .
```

# Run Container

``` bash
sudo docker run --name=test1 -h test1 --mac-address=00:e0:81:78:7b:a8 -d -p 11111:22 -p 11100:1700 -p 11101:1701 -v /apps/:/apps license-env:v1
sudo docker run --name=test2 -h test2 --mac-address=00:e0:81:78:7b:a8 -d -p 22222:22 -p 22200:1700 -p 22201:1701 -v /apps/:/apps license-env:v1

```

# License Server Configuration

## License Server1

``` bash
SERVER test1 00E081787BA8 1700
DAEMON lsf_ld /apps/jhlm_test_pack/lsf_ld port=1701
...
```

## License Server2

``` bash
SERVER test2 00E081787BA8 1700
DAEMON lsf_ld /apps/jhlm_test_pack/lsf_ld port=1701
...
```

## License Usage

Run the following command on physical host.

``` bash
./lmutil lmstat -c 11100@localhost -a
./lmutil lmstat -c 22200@localhost -a

./friendly -c 11100@localhost -f "<feature> <num>"
./friendly -c 22200@localhost -f "<feature> <num>"

./lmutil lmstat -c 1700@172.17.0.2 -a
./lmutil lmstat -c 1700@172.17.0.3 -a

./friendly -c 1700@172.17.0.2 -f "<feature> <num>"
./friendly -c 1700@172.17.0.3 -f "<feature> <num>"
```
