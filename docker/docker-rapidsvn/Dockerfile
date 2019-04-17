from ubuntu

RUN apt-get update
RUN apt-get install -y iputils-ping net-tools rapidsvn
RUN apt-get install -y subversion locales
RUN apt-get install -y sudo language-pack-zh-hans language-pack-gnome-zh-hans
RUN apt-get install -y fonts-wqy-microhei fonts-wqy-zenhei
RUN apt-get install -y diffuse

RUN echo 'root:Letmein' | chpasswd
RUN useradd -u 1000 jhadmin
RUN echo "jhadmin:jhadmin" | chpasswd
RUN echo "jhadmin   ALL=(ALL)       NOPASSWD: ALL" >> /etc/sudoers

RUN locale-gen en_US.UTF-8
