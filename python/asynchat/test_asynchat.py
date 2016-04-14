import asyncore
import asynchat
import socket


class HTTPChannel(asynchat.async_chat):

    def __init__(self, sock, addr):
        asynchat.async_chat.__init__(self, sock)
        self.set_terminator('\r\n')
        self.request = None
        self.response = None
        self.data = None
        self.shutdown = 0

    def collect_incoming_data(self, data):
#         print('Incoming data: ----- begin -----')
#         print(data)
#         print('Incoming data: ----- end -----')
        self.data = data

    def found_terminator(self):
        if not self.request:
            self.request = self.data.split(None, 2)
            if len(self.request) != 3:
                self.shutdown = 1
            else:
                name = self.request[1].replace('/', '', 1)
            self.response = 'Hello %s! %s' % (name, '\r\n')
            self.set_terminator('\r\n\r\n')
        else:
            self.push('HTTP/1.0 200 OK\r\n')
            self.push('Content-type: text/html\r\n')
            self.push('\r\n')
            
            self.push('<html>\r\n')
            self.push('<body>\r\n')
            self.push(self.response)
            self.push('</body>\r\n')
            self.push('</html>\r\n')
            self.close_when_done()

class HTTPServer(asyncore.dispatcher):

    def __init__(self):
        asyncore.dispatcher.__init__(self)
        
        self.port = 8888
        self.create_socket(socket.AF_INET, socket.SOCK_STREAM)
        self.bind(("", self.port))
        self.listen(5)
        print 'Serving at port', self.port

    def handle_accept(self):
        conn, addr = self.accept()
        HTTPChannel(conn, addr)


s = HTTPServer()
asyncore.loop()
