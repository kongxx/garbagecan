# 使用pdfminer解析pdf文件

最近要做个从 pdf 文件中抽取文本内容的工具，大概查了一下 python 里可以使用 pdfminer 来实现。下面就看看怎样使用吧。

## 安装
python的工具，安装当然是使用pip安装了。
``` shell
pip install pdfminer
```

## 命令行方式使用
为了使用方便，pdfminer 提供了一个命令行工具来直接转换pdf文件，使用方法如下：
``` shell
pdf2txt.py <path_to_pdf_file>
```

## 编程方式使用
除了命令行方式以外，对于复杂应用场景，pdfminer 也提供了以编程方式来转换 pdf 文件，主要使用下面几个类来实现：

- PDFParser： 用来解析pdf文件。
- PDFDocument：用来保存 PDFParser 解析后的对象。
- PDFPageInterpreter：用来处理解析后的文档页面内容。
- PDFResourceManager：pdf 共享资源管理器,用于存储共享资源，如字体或图像。

下面看一个例子：

``` shell
#!/usr/bin/env python
# -*- coding: utf-8 -*-
from pdfminer.pdfparser import PDFParser
from pdfminer.pdfdocument import PDFDocument
from pdfminer.pdfpage import PDFPage, PDFTextExtractionNotAllowed
from pdfminer.pdfinterp import PDFResourceManager, PDFPageInterpreter
from pdfminer.converter import PDFPageAggregator
from pdfminer.layout import LAParams
import StringIO


class PDFUtils():

    def __init__(self):
        pass

    def pdf2txt(self, path):
        output = StringIO.StringIO()
        with open(path, 'rb') as f:
            praser = PDFParser(f)

            doc = PDFDocument(praser)

            if not doc.is_extractable:
                raise PDFTextExtractionNotAllowed

            pdfrm = PDFResourceManager()

            laparams = LAParams()

            device = PDFPageAggregator(pdfrm, laparams=laparams)

            interpreter = PDFPageInterpreter(pdfrm, device)

            for page in PDFPage.create_pages(doc):
                interpreter.process_page(page)
                layout = device.get_result()
                for x in layout:
                    if hasattr(x, "get_text"):
                        content = x.get_text()
                        output.write(content)

        content = output.getvalue()
        output.close()
        return content


if __name__ == '__main__':
    path = u'/tmp/abc.pdf'
    pdf_utils = PDFUtils()
    print pdf_utils.pdf2txt(path)
```
## 参考

> http://www.unixuser.org/~euske/python/pdfminer/index.html