import socket
import threading


class MyThread(threading.Thread):
    def __init__(self, sock, addr):
        self.sock = sock
        self.addr = addr
        threading.Thread.__init__(self);

    def run(self):
        print 'Accept new connection from %s: %s...' % self.addr
        self.sock.send('Welcome!')
        while True:
            data = self.sock.recv(1024)
            if data == 'exit' or not data:
                break
            self.sock.send('Hello, %s!' % data)
        self.sock.close()
        print 'Connection from %s: %s closed.' % self.addr

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind(('0.0.0.0', 9999))
s.listen(5)
print 'Waiting for connection...'

while True:
    sock, addr = s.accept()
    thread = MyThread(sock, addr)
    thread.start()
