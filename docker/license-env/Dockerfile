FROM gpmidi/centos-6.5

MAINTAINER Fanbin Kong "kongxx@hotmail.com"

ENV LANG en_US.UTF-8
ENV LC_ALL en_US.UTF-8

RUN yum install -y openssh-server sudo tar

RUN sed -i 's/UsePAM yes/UsePAM no/g' /etc/ssh/sshd_config
RUN echo "root:Letmein" | chpasswd

RUN ssh-keygen -t dsa -f /etc/ssh/ssh_host_dsa_key
RUN ssh-keygen -t rsa -f /etc/ssh/ssh_host_rsa_key
RUN mkdir /var/run/sshd

EXPOSE 22
CMD ["/usr/sbin/sshd", "-D"]
