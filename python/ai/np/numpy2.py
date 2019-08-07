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

test1()
test2()
