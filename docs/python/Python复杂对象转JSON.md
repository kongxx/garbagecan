# Python复杂对象转JSON

在Python对于简单的对象转json还是比较简单的，如下：

``` python
import json

d = {'a': 'aaa', 'b': ['b1', 'b2', 'b3'], 'c': 100}
json_str = json.dumps(d)
print json_str
```

对于复杂对象，可以使用下面的方法来实现，比如：

``` python
import json

class Customer: 
    def __init__(self, name, grade, age, home, office): 
        self.name = name 
        self.grade = grade 
        self.age = age 
        self.address = Address(home, office)
    def __repr__(self): 
        return repr((self.name, self.grade, self.age, self.address.home, self.address.office)) 

class Address:
    def __init__(self, home, office):
        self.home = home
        self.office = office
    def __repr__(self): 
        return repr((self.name, self.grade, self.age))
    
customers = [ 
        Customer('john', 'A', 15, '111', 'aaa'), 
        Customer('jane', 'B', 12, '222', 'bbb'), 
        Customer('dave', 'B', 10, '333', 'ccc'), 
        ]

json_str = json.dumps(customers, default=lambda o: o.__dict__, sort_keys=True, indent=4)
print json_str
```

结果如下
``` python
[
    {
        "address": {
            "home": "111", 
            "office": "aaa"
        }, 
        "age": 15, 
        "grade": "A", 
        "name": "john"
    }, 
    {
        "address": {
            "home": "222", 
            "office": "bbb"
        }, 
        "age": 12, 
        "grade": "B", 
        "name": "jane"
    }, 
    {
        "address": {
            "home": "333", 
            "office": "ccc"
        }, 
        "age": 10, 
        "grade": "B", 
        "name": "dave"
    }
]
```

``` java
```

``` java
```
