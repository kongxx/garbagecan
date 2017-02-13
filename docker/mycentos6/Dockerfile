FROM gpmidi/centos-6.5

MAINTAINER Fanbin Kong "kongxx@hotmail.com"

RUN yum groupinstall -y "Development Tools"
RUN yum install -y sudo tar wget epel-release subversion \
	openssh-server openssh-clients openssl openssl-devel \
	python-setuptools

RUN sed -i 's/UsePAM yes/UsePAM no/g' /etc/ssh/sshd_config \
	&& echo 'root:jhadmin' | chpasswd \
	&& useradd -u 1000 jhadmin \
	&& echo "jhadmin:jhadmin" | chpasswd \
	&& echo "jhadmin   ALL=(ALL)       NOPASSWD: ALL" >> /etc/sudoers \
	&& ssh-keygen -t dsa -f /etc/ssh/ssh_host_dsa_key \
	&& ssh-keygen -t rsa -f /etc/ssh/ssh_host_rsa_key \
	&& mkdir /var/run/sshd
