import numpy as np


def test1():
    a = np.eye(3)
    print a


def test2():
    a = np.arange(24)
    print a
    print np.mean(a)
    print np.median(a)
    print np.sort(a)
    print np.where(a > 10)


def test3():
    a = np.linspace(-1, 1, 100)
    print a.shape
    b = a[:, np.newaxis]
    print b.shape
    c = a.reshape(100, 1)
    print c.shape

test1()
test2()
test3()
