
docker build -t kongxx/rapidsvn:v1 .

# docker run -it --rm -e DISPLAY=$DISPLAY -v /tmp/.X11-unix:/tmp/.X11-unix -v /opt/.myenv:/opt/.myenv kongxx/rapidsvn:v1 rapidsvn

# docker run -it --rm -e DISPLAY=$DISPLAY -v /tmp/.X11-unix:/tmp/.X11-unix -v /home/jhadmin:/home/jhadmin kongxx/rapidsvn:v1 bash

xhost +

docker run -it --rm -u jhadmin -e DISPLAY=$DISPLAY -e LANG=zh_CN.UTF-8 -v /tmp/.X11-unix:/tmp/.X11-unix -v /home/jhadmin:/home/jhadmin kongxx/rapidsvn:v1 bash

docker run -it --rm -u jhadmin -e DISPLAY=$DISPLAY -e LANG=zh_CN.UTF-8 -v /tmp/.X11-unix:/tmp/.X11-unix -v /home/jhadmin:/home/jhadmin kongxx/rapidsvn:v1 rapidsvn


---

diff tool: diffuse
