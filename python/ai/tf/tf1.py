addimport tensorflow as tf
import numpy as np
import matplotlib.pyplot as plt

# create data
x_data = np.random.rand(100).astype(np.float32)
y_data = x_data * 0.1 + 0.3

print "x_data: %s" % x_data
print "y_data: %s" % y_data

Weights = tf.Variable(tf.random_uniform([1], -1.0, 1.0))
Biases = tf.Variable(tf.zeros([1]))

y = Weights * x_data + Biases

loss = tf.reduce_mean(tf.square(y - y_data))

optimizer = tf.train.GradientDescentOptimizer(0.5)
train = optimizer.minimize(loss)

init = tf.global_variables_initializer()
sess = tf.Session()
sess.run(init)

print "Weights: %s" % sess.run(Weights)

for step in range(100):
    sess.run(train)
    if step % 20 == 0:
        print(step, sess.run(Weights), sess.run(Biases))

w = sess.run(Weights)
b = sess.run(Biases)
sess.close()

plt.figure()
plt.plot(x_data, y_data, 'ro', label='Original data')
plt.plot(x_data, w * x_data + b, '--', label='Fitted line')
plt.legend()
plt.show()
