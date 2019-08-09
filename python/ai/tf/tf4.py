#!/usr/bin/env python
# -*- coding: utf-8 -*-
import numpy as np
import tensorflow as tf
from sklearn.model_selection import train_test_split
import matplotlib.pyplot as plt

# a = np.linspace(-1, 1, 100)
# print a.shape
# b = a[:, np.newaxis]
# print b.shape
# c = a.reshape(100, 1)
# print c.shape


def add_layer(inputs, in_size, out_size, activation_function=None):
    weights = tf.Variable(tf.random_normal([in_size, out_size]))
    biases = tf.Variable(tf.zeros([1, out_size]) + 0.1)
    outputs = tf.matmul(inputs, weights) + biases
    if activation_function is not None:
        outputs = activation_function(outputs)
    return outputs


np.set_printoptions(precision=4, suppress=True)

data_path = r'/opt/.myenv/data.csv'
data = np.genfromtxt(data_path, delimiter=',')[1:, :]

x = data[:, 0: 3]
y = data[:, 3: 9]

x = x / 10.0
y[:, 3] = y[:, 3] / max(y[:, 3])
y[y == 0] = np.random.uniform(0.0001, 0.001)
print y[:, 3]
print max(y[:, 3])

x_train, x_test, y_train, y_test = train_test_split(x, y, test_size=0.2)
print x_train.shape, x_test.shape, y_train.shape, y_test.shape
# x_train = x[0:150, :]
# x_test  = x[150:, :]
# y_train = y[0:150, :]
# y_test = y[150:, :]
# print x_train.shape, x_test.shape, y_train.shape, y_test.shape

xs = tf.placeholder(tf.float32, [None, 3])
ys = tf.placeholder(tf.float32, [None, 6])

layer1 = add_layer(xs, 3, 32, activation_function=tf.nn.relu)
layer2 = add_layer(layer1, 32, 32, activation_function=tf.nn.tanh)
prediction = add_layer(layer2, 32, 6, activation_function=None)

# loss = tf.reduce_mean(tf.reduce_sum(tf.square(ys - prediction), reduction_indices=[1]))
loss = tf.reduce_mean(tf.square(ys - prediction))
train = tf.train.GradientDescentOptimizer(0.01).minimize(loss)

init = tf.global_variables_initializer()

with tf.Session() as sess:
    sess.run(init)

    for i in range(5000):
        sess.run(train, feed_dict={xs: x_train, ys: y_train})
        if i % 100 == 0:
            print(sess.run(loss, feed_dict={xs: x_train, ys: y_train}))

    prediction_value = sess.run(prediction, feed_dict={xs: x_test})

    for i in range(0, len(y_test)):
        print '----------'
        print y_test[i]
        print prediction_value[i]

titles = [u'阻力', u'升力', u'侧向力', u'滚转力矩', u'偏航力矩', u'俯仰力矩']
x_value = np.arange(1, len(y_test) + 1, 1)
fig = plt.figure()
for i in range(1, 7):
    ax = fig.add_subplot(2, 3, i)
    ax.plot(x_value, y_test[:, i - 1:i], label='test value')
    ax.plot(x_value, prediction_value[:, i-1:i], label='prediction value')
    ax.set_title('test')
plt.show()
