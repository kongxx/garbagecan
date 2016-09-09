FROM gpmidi/centos-6.5

MAINTAINER Fanbin Kong "kongxx@hotmail.com"

RUN yum install -y sudo passwd tar wget openssh-server openssh-clients openssl openssl-devel subversion
RUN yum groupinstall -y 'Development tools'

RUN yum install -y openmpi-1.8.x86_64 openmpi-1.8-devel.x86_64
RUN yum install -y openmpi-1.10.x86_64 openmpi-1.10-devel.x86_64
RUN yum install -y mpich2.x86_64 mpich2-devel.x86_64
RUN yum install -y environment-modules

RUN yum install -y python-setuptools
RUN easy_install pip

RUN sed -i 's/UsePAM yes/UsePAM no/g' /etc/ssh/sshd_config
RUN echo 'root:Letmein' | chpasswd
RUN useradd jhadmin
RUN echo "jhadmin:jhadmin" | chpasswd
RUN echo "jhadmin   ALL=(ALL)       NOPASSWD: ALL" >> /etc/sudoers

RUN ssh-keygen -t dsa -f /etc/ssh/ssh_host_dsa_key
RUN ssh-keygen -t rsa -f /etc/ssh/ssh_host_rsa_key
RUN mkdir /var/run/sshd

EXPOSE 22
CMD ["/usr/sbin/sshd", "-D"]
