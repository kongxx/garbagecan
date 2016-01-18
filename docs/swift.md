# Swift中使用presentViewController跳转页面后模拟器显示黑屏问题

## 问题原因
针对storyboard制作页面和手写页面，需要使用两种不同方法进行页面跳转。

## 解决办法
针对手写页面及storyboard制作页面，使用代码进行页面跳转的两种方法。

### 对于使用storyboard制作的页面
``` swift
var sb = UIStoryboard(name: "Main", bundle:nil)
var vc = sb.instantiateViewControllerWithIdentifier("myViewController") as MyViewController
self.presentViewController(vc, animated: true, completion: nil)
```
其中 myViewController 是在 Main.storyboard 中选中的 MyViewController 的 storyboardID 值。可以在 Identifier inspector 中修改。


### 对于手写页面
``` swift
var vc = MyViewController()
self.presentViewController(vc, animated: true, completion: nil)
```

#######################################################
# Swift设置Table View的Cell中Lable自适应内容高度的

@IBOutletweak var myTableView: UITableView!

override func viewDidLoad() {
    super.viewDidLoad()
    myTableView.estimatedRowHeight = 44.0
    myTableView.rowHeight =UITableViewAutomaticDimension
}

然后修改在Table Cell中Label的lines属性，将其设置为0。

#######################################################


