# 使用Nodejs和Express构建http响应流实现下载功能

首先创建一个文件流来读取要下载的文件，当然可以是动态产生的输入流
``` javascript
const fileStream = fs.createReadStream('test.zip');
```

然后创建响应头，指定响应的类型，同时也可以使用Content-Disposition设置浏览器下载时需要保存的文件名
``` javascript
const head = {
	'Content-Type': 'application/zip, application/octet-stream; charset=ISO-8859-1',
	'Content-Disposition': 'attachment;filename=\"resources123.zip\"'
  };
res.writeHead(200, head);
```

最后通过文件流的pipe()方法输出到响应里
``` javascript
fileStream.pipe(res);
```

完整router代码如下
``` javascript
router.get('/download', function(req, res, next) {
    const fileStream = fs.createReadStream('test.zip');
    const head = {
        'Content-Type': 'application/zip, application/octet-stream; charset=ISO-8859-1',
        'Content-Disposition': 'attachment;filename=\"test123.zip\"'
      };
    res.writeHead(200, head);
    fileStream.pipe(res);
});
```

最后说一下，express里的response也提供了一个attachment()方法，这个方法会设置Content-Disposition头，并且会通过res.type()来设置Content-Type头，代码如下
``` javascript
router.get('/download', function(req, res, next) {
    const fileStream = fs.createReadStream('test.zip');
    res.attachment('test123.zip');
    fileStream.pipe(res);
});
```

最后的最后，看一下如果出错了应该怎么处理
``` javascript
router.get('/download', function(req, res, next) {
    const fileStream = fs.createReadStream('test.zip');
    fileStream.on('open', () => {
        res.attachment('test123.csv');
        fileStream.pipe(res);
    });
    fileStream.on('error', err => {
        next(err);
    });
});
```
