# 基于jieba库实现中文词频统计

要实现中文分词功能，大家基本上都是在使用 jieba 这个库来实现，下面就看看怎样实现一个简单文本分词功能。

## 安装
python的工具，安装当然是使用pip安装了。
``` shell
pip install jieba
```

## 使用

先看一个小例子，下面的代码是从一个文本文件中分词并统计出现频率最高的10个单词，并打印到控制台。
``` shell
#!/usr/bin/env python
# -*- coding: utf-8 -*-
import jieba
import jieba.analyse
import codecs
import re
from collections import Counter


class WordCounter(object):

    def count_from_file(self, file, top_limit=0):
        with codecs.open(file, 'r', 'utf-8') as f:
            content = f.read()
            content = re.sub(r'\s+', r' ', content)
            content = re.sub(r'\.+', r' ', content)
            return self.count_from_str(content, top_limit=top_limit)

    def count_from_str(self, content, top_limit=0):
        if top_limit <= 0:
            top_limit = 100
        tags = jieba.analyse.extract_tags(content, topK=100)

        words = jieba.cut(content)
        counter = Counter()
        for word in words:
            if word in tags:
                counter[word] += 1

        return counter.most_common(top_limit)


if __name__ == '__main__':
    counter = WordCounter()
    result = counter.count_from_file(r'/tmp/abc.txt', top_limit=10)
    for k, v in result:
        print k, v
```

代码说明：
1. 代码首先从一个文本文件读入文本，并作了一些简单的替换，比如替换多个空格为单空格等。
2. 使用关键词提取功能，提取权重最高的10个关键词。
3. 使用精确模式对文件内容分词。
4. 根据关键词和分词结果，统计词频。
5. 排序并返回词频最高的单词和出现次数。

## 多说两句

### 分词模式
jieba 分词有三种模式：精确模式、全模式和搜索引擎模式，且分词结果返回的是一个生成器。：
- 精确模式: jieba.cut(str)  默认实现。
- 全模式: jieba.cut(str, cut_all=True) 全模式是把文本分成尽可能多的词。
- 搜索引擎模式: jieba.cut_for_search(str, cut_all=True)

### 关键词提取功能
jieba提供了关键词提取功能，使用方法如下：
``` python
jieba.analyse.extract_tags(sentence, topK=20, withWeight=False, allowPOS=())
sentence 为待提取的文本
topK 为返回几个 TF/IDF 权重最大的关键词，默认值为 20
withWeight 为是否一并返回关键词权重值，默认值为 False
allowPOS 仅包括指定词性的词，默认值为空，即不筛选
```

### 使用并行分词模式
``` python
# 开启并行分词模式，参数为并发执行的进程数
jieba.enable_parallel(5)

# 关闭并行分词模式
jieba.disable_parallel()
```

### 使用用户字典分词
``` python
jieba.load_userdict('user_dict.txt')
```
