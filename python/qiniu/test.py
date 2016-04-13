#!/usr/bin/env python
# -*- coding: utf-8 -*-
from qiniu import Auth
from qiniu import put_file
from qiniu import BucketManager
import requests

access_key='hhdIj7BhA7qhtEnan-xBc9FovmMWJgdLFVpOwYNY'
secret_key='SelkMUKqogBdVA_gYkhlTi57MQ2ly5YTQqU9e_jW'
bucket='test'
# mykey='mykey'
mykey='a/b/c'


def test_upload():
    q = Auth(access_key, secret_key)
    token = q.upload_token(bucket, mykey.encode('utf-8'))
    file = '/tmp/abc.txt'
    ret, info = put_file(token, mykey.encode('utf-8'), file, mime_type="text/plain", check_crc=True)
    print(info)
    print(ret)


def test_delete():
    q = Auth(access_key, secret_key)
    bm = BucketManager(q)
    ret, info = bm.delete(bucket, mykey)
    print(info)
    print(ret)


def test_fetch():
    q = Auth(access_key, secret_key)
    bm = BucketManager(q)
    ret, info = bm.stat(bucket, mykey)
    print(info)
    print(ret)

    bucket_domain = '7xpb8s.com1.z0.glb.clouddn.com'
    base_url = 'http://%s/%s' % (bucket_domain, mykey)
    private_url = q.private_download_url(base_url, expires=3600)
    print(private_url)
    r = requests.get(private_url)
    assert r.status_code == 200

    ret, info = bm.fetch(private_url, bucket, mykey)
    print(info)
    print(ret)


# test_upload()
# test_delete()
# test_fetch()
