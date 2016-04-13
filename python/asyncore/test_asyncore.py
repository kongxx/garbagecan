import time
import asyncore
import socket
import threading


class EchoHandler(asyncore.dispatcher_with_send):

    def handle_read(self):
        data = self.recv(1024)
        if data:
            self.send(data)

class EchoServer(asyncore.dispatcher):

    def __init__(self, host, port):
        asyncore.dispatcher.__init__(self)
        self.create_socket(socket.AF_INET, socket.SOCK_STREAM)
        self.set_reuse_addr()
        self.bind((host, port))
        self.listen(1)

    def handle_accept(self):
        conn, addr = self.accept()
        print 'Incoming connection from %s' % repr(addr)
        self.handler = EchoHandler(conn)

class EchoClient(asyncore.dispatcher):

    def __init__(self, host, port):
        asyncore.dispatcher.__init__(self)
        self.messages = ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10']
        self.create_socket(socket.AF_INET, socket.SOCK_STREAM)
        self.connect((host, port))

    def handle_connect(self):
        pass

    def handle_close(self):
        self.close()

    def handle_read(self):
        print self.recv(1024)

    def writable(self):
        return (len(self.messages) > 0)

    def handle_write(self):
        if len(self.messages) > 0: 
            self.send(self.messages.pop(0))

class EchoServerThread(threading.Thread):
    def __init__(self):
        threading.Thread.__init__(self)

    def run(self):
        server = EchoServer('localhost', 9999)
        asyncore.loop()
        
class EchoClientThread(threading.Thread):
    def __init__(self):
        threading.Thread.__init__(self)

    def run(self):
        client = EchoClient('localhost', 9999)
        asyncore.loop()

EchoServerThread().start()
time.sleep(2)
EchoClientThread().start()
