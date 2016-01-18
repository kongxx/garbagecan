
# 在 Django REST framework 善用 SerializerMethodField 来优化不必要的查询

首先来看一个例子，在一般情况下，对于有父子关系的对象，我们使用下面的方法来创建类。
一个Article类，一个Article对象可以有多个Comment实例，那么Django中类的定义如下：
``` python
#coding:utf-8
from django.db import models
from django.contrib.auth.models import User

class Article(models.Model):
    title = models.CharField('文章标题', max_length=1024, blank=False)
    summary = models.TextField('文章简介', blank=False)
    content = models.TextField('文章内容', blank=True, null=True)
    create_user = models.ForeignKey(User, related_name='article_create_user', verbose_name='创建用户')
    create_time = models.DateTimeField('创建时间', auto_now_add=True)

    def __str__(self):
        return self.title

    def __unicode__(self):
        return self.title

    class Meta:
        ordering = ('-create_time',)

class Comment(models.Model):
    article = models.ForeignKey(Article, related_name='article_comments', verbose_name='文章')
    comment = models.CharField('评论', max_length=1024, blank=False)
    create_user = models.ForeignKey(User, related_name='article_comments_create_user', verbose_name='创建用户')
    create_time =models.DateTimeField('创建时间', auto_now_add=True)

    def __str__(self):
        return self.comment

    def __unicode__(self):
        return self.comment

    class Meta:
        ordering = ('-create_time',)
```
根据上面类的定义，那么在 Django REST framework 我们通常按下面的方式定义 Serializer 类。
在 ArticleSerializer 类中增加一个属性 article_comments 来保存当前 Article 对象所有的评论集合。但是此时有个问题如果此 Article 对象所拥有的 Comment 比较多，此时就会影响性能。比如：
``` python
#coding:utf-8
from rest_framework import serializers
from .models import Article, Comment

class ArticleSerializer(serializers.ModelSerializer):
    article_comments = serializers.PrimaryKeyRelatedField(many=True, required=False, read_only=True)
    
    class Meta:
        model = Article
        fields = ('id', 'title', 'summary', 'content', 'create_user', 'create_time', 'article_comments')


class CommentSerializer(serializers.ModelSerializer):
    
    class Meta:
        model = Comment
        fields = ('id', 'article', 'comment', 'create_user', 'create_time')
```
其实在很多时候我们并不需要在查询 Article 对象的时候查询所拥有的 Comment 对象，很多时候我们只是需要一个 Article 所拥有 Comment 对象的总数就可以了，如果有需要再去查询 Comment 列表详细。此时我们就可以使用 Django REST framework 提供的 SerializerMethodField 来实现这个目的。如下：
```python
#coding:utf-8
from rest_framework import serializers
from .models import Article, Comment

class ArticleSerializer(serializers.ModelSerializer):
    article_comments_count = serializers.SerializerMethodField()
    
    class Meta:
        model = Article
        fields = ('id', 'title', 'summary', 'content', 'create_user', 'create_time', 'article_comments_count')

    def get_article_comments_count(self, obj):
        return obj.article_comments.all().count()


class CommentSerializer(serializers.ModelSerializer):
    
    class Meta:
        model = Comment
        fields = ('id', 'article', 'comment', 'create_user_id', 'create_user_name', 'create_time')
```

* 首先在 ArticleSerializer 中去除 article_comments 属性；
* 然后在 ArticleSerializer 中增加一个属性 article_comments_count，并把这个属性添加到 Meta 的 fields 列表里；
* 添加一个 get_article_comments_count 方法，这个方法的命名规则就是在上面声明的属性前面加上个 “get_” 前缀，并接受一个 obj 参数，这个 obj 参数就是当前的 Article 对象实例。

此时在查看 Article 的api中就只显示 Comment 的总数而不显示具体列表了。
