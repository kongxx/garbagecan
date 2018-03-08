import urllib2
import time
import os


def upload():
    boundary = '----------%s' % hex(int(time.time() * 1000))

    data = []
    data.append('--%s' % boundary)

    filename = 'test.txt'
    # filename = 'test.tar.gz'
    filepath = os.path.join('/home/kongxx/test/logs', filename)
    f = open(filepath, "rb")
    chunk = f.read()
    f.close()

    data.append('Content-Disposition: form-data; name="user.name"')
    data.append('Content-Type: text/plain; charset=UTF-8')
    data.append('\r\n')
    data.append('kongxx')
    data.append('--%s' % boundary)

    data.append('Content-Disposition: form-data; name="myFile"; filename=%s' % filename)
    data.append('Content-Type: application/octet-stream')
    data.append('\r\n')
    data.append(chunk)
    data.append('--%s--\r\n' % boundary)

    http_body = '\r\n'.join(data)
    print http_body

    url = "http://localhost:9999/upload/doUpload.action"
    req = urllib2.Request(url, data=http_body)

    req.add_header('Content-Type', 'multipart/form-data; boundary=%s' % boundary)
    req.add_header('Content-Length', len(http_body))

    urllib2.urlopen(req, timeout=5)


upload()
