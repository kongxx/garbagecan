import numpy as np

def test1():
    a = np.array([[1, 2, 3], [4, 5, 6]])
    print a
    print a.shape
    print a.dtype
    print a.dtype.itemsize

    b = a.T
    print b
    print b.shape

    c = np.arange(24).reshape(4, 6)
    print c
    print c.ravel()
    print c.flatten()
    c1 = c.flatten()
    c1.shape = (6, 4)
    print c1

    c2 = c1.transpose()
    print c2

def test2():
    a = np.arange(9).reshape(3, 3)
    print a
    b = a ** 2
    print b

    print 'hstack ----------'
    print np.hstack((a, b))
    print np.concatenate((a, b), axis=1)

    print 'vstack ----------'
    print np.vstack((a, b))
    print np.concatenate((a, b), axis=0)

    print 'dstack ----------'
    print np.dstack((a, b))

    print 'column_stack ----------'
    print np.column_stack((a, b))

    print 'row_stack ----------'
    print np.row_stack((a, b))

def test3():
    a = np.arange(9).reshape(3, 3)
    print a

    print 'hsplit ----------'
    print np.hsplit(a, 3)

    print 'vsplit ----------'
    print np.vsplit(a, 3)

def test4():
    a = np.arange(9).reshape(3, 3)
    print a

    print a.shape
    print a.ndim
    print a.size
    print a.itemsize

test1()
# test2()
# test3()
test4()
