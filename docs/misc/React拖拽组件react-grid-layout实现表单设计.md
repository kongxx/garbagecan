# React拖拽组件react-grid-layout实现表单设计

最近在看在线表单设计，找了一些现成的产品和库，今天就看看怎样使用 React-Grid-Layout 实现表单设计。

React-Grid-Layout是一个基于 react 的网格布局系统，可实现基于表格的拖拽功能。

## 创建工程

``` shell
npx create-react-app myapp
```

## 安装依赖库

``` shell
npm install react-grid-layout
```

另外例子还使用了boostrap做渲染，因此还需要安装 boostrap 和 react-bootstrap。

``` shell
npm install bootstrap
npm install react-bootstrap
```

## 代码实现（最后附完整 App.js 实现代码）

看一下要实现的功能和布局：
- 左边是个控件列表，这里只放了三个控件:input, password和select；这里的控件需要增加 draggable 属性，标识控件可拖拽，比如：

``` shell
<Button variant="primary" name="input"
    draggable={true}
    unselectable="on"
    onDragStart={onDragStartForDraggable}>
    Input
</Button>
```

- 右边是个布局区域，可以在上面拖拽摆放控件位置，使用 react-grid-layout 的 Responsive 实现。当每个左侧控件拖到这个区域后，将根据具体类型，展示位具体样式。

下面看一下代码实现，首先初始化三个控件，用来默认摆放着右侧的布局区域内

``` shell
  # 初始化三个控件
  let items = ["input", "password", "select"];
  
  # 初始布局，其中i对应上面的三个控件的名字，x表示横向位置，y表示纵向位置，w表示宽度，h表示高度
  let layout = [
    { i: "input", x: 0, y: 0, w: 5, h: 1, isResizable: false },
    { i: "password", x: 0, y: 1, w: 5, h: 1, isResizable: false },
    { i: "select", x: 0, y: 1, w: 5, h: 1, isResizable: false },
  ];
```

- 定义右侧拖拽区域（GridLayout控件）的默认属性

``` shell
  const defaultProps = {
    className: "layout",
    rowHeight: 40,
    onLayoutChange: function(data) {},
    breakpoints: { lg: 1200, md: 996, sm: 768, xs: 480, xxs: 0 },
    cols: { lg: 1, md: 1, sm: 1, xs: 1, xxs: 1 },
    layouts: { lg: layout }
  };
```

- 定义GridLayout控件参数

``` shell
  const [state, setState] = useState({
    currentBreakpoint: "lg",
    compactType: "vertical",
    mounted: false,
    items: items,
    layouts: { lg: layout }
  });
```

- 实现左侧控件的拖拽事件，主要是记住当前拖拽的是那个控件

``` shell
  const onDragStartForDraggable = (e) => {
    currentDraggable = e.target.name + "_" + Date.now();
    e.dataTransfer.setData("text/plain", "");
  };
```

- 实现右侧区域的的拖拽事件，主要是根据当前拖拽控件，设置名字和位置

``` shell
  const onDrop = (layout, layoutItem, _event) => {
    layout = layout.toSorted((a, b) => a.y - b.y); 
    let newItems = [];
    layout.forEach((item) => {
      if (item.i === '__dropping-elem__') {
        item.i = currentDraggable;
      }
      item.isResizable = false;
      newItems.push(item.i);
    });
    layoutItem.i = currentDraggable;
    layoutItem.isResizable = false;

    setState({
      ...state,
      items: newItems,
      layouts: { lg: layout }
    });
    console.log("onDrop layout: ", layout);
    console.log("onDrop layoutItem: ", layoutItem);
  };
```

- 控件标签代码

``` shell
<ResponsiveGridLayout
    {...defaultProps}
    className="layout"
    rowHeight={40}
    width={800}
    onDrop={onDrop}
    onLayoutChange={onLayoutChange}
    measureBeforeMount={false}
    useCSSTransforms={state.mounted}
    compactType={state.compactType}
    preventCollision={!state.compactType}
    isDroppable={true}
    >
{state.items.map(el => {
    if (el.startsWith('input')) {
        ...
    } if (el.startsWith('password')) {
        ...
    } else if (el.startsWith('select')) {
        ...
    } else {
        ...
    }
})}
</ResponsiveGridLayout>
```

完整 App.js 代码如下：

``` javascript
import { useState } from 'react';
import { Form, Container, Stack, Row, Col, Button, Accordion } from "react-bootstrap";
import { Responsive as ResponsiveGridLayout } from "react-grid-layout";

import logo from './logo.svg';
import './App.css';
import "bootstrap/dist/css/bootstrap.min.css";
import "react-grid-layout/css/styles.css";
import "react-resizable/css/styles.css";

function App() {

  let items = ["input", "password", "select"];

  let layout = [
    { i: "input", x: 0, y: 0, w: 5, h: 1, isResizable: false },
    { i: "password", x: 0, y: 1, w: 5, h: 1, isResizable: false },
    { i: "select", x: 0, y: 1, w: 5, h: 1, isResizable: false },
  ];

  let currentDraggable = '';

  const [state, setState] = useState({
    currentBreakpoint: "lg",
    compactType: "vertical",
    mounted: false,
    items: items,
    layouts: { lg: layout }
  });
  
  const defaultProps = {
    className: "layout",
    rowHeight: 40,
    onLayoutChange: function(data) {},
    breakpoints: { lg: 1200, md: 996, sm: 768, xs: 480, xxs: 0 },
    cols: { lg: 1, md: 1, sm: 1, xs: 1, xxs: 1 },
    layouts: { lg: layout }
  };

  const onLayoutChange = (layout, layouts) => {
    console.log("onLayoutChange layout: ", layout);
    console.log("onLayoutChange layouts: ", layouts);
  };

  const onDrop = (layout, layoutItem, _event) => {
    layout = layout.toSorted((a, b) => a.y - b.y); 
    let newItems = [];
    layout.forEach((item) => {
      if (item.i === '__dropping-elem__') {
        item.i = currentDraggable;
      }
      item.isResizable = false;
      newItems.push(item.i);
    });
    layoutItem.i = currentDraggable;
    layoutItem.isResizable = false;

    setState({
      ...state,
      items: newItems,
      layouts: { lg: layout }
    });
    console.log("onDrop layout: ", layout);
    console.log("onDrop layoutItem: ", layoutItem);
  };

  const onDragStartForDraggable = (e) => {
    currentDraggable = e.target.name + "_" + Date.now();
    e.dataTransfer.setData("text/plain", "");
  };

  return (
    <div className="App">
      <Container>
        <Row>
          <Col sm>
            <Accordion defaultActiveKey="0">
              <Accordion.Item eventKey="0">
                <Accordion.Header>Components</Accordion.Header>
                <Accordion.Body>
                  <Stack gap={2}>
                    <Button variant="primary" name="input"
                      draggable={true}
                      unselectable="on"
                      onDragStart={onDragStartForDraggable}>
                      Input
                    </Button>
                    <Button variant="primary" name="password"
                      draggable={true}
                      unselectable="on"
                      onDragStart={onDragStartForDraggable}>
                      Password
                    </Button>
                    <Button variant="primary" name="select"
                      draggable={true}
                      unselectable="on"
                      onDragStart={onDragStartForDraggable}>
                      Select
                    </Button>
                  </Stack>
                </Accordion.Body>
              </Accordion.Item>
            </Accordion>
          </Col>
          <Col sm={9}>
            <Accordion defaultActiveKey="0">
              <Accordion.Item eventKey="0">
                <Accordion.Header>Form</Accordion.Header>
                <Accordion.Body>
                <Form>
                  <ResponsiveGridLayout
                    {...defaultProps}
                    className="layout"
                    rowHeight={40}
                    width={800}
                    onDrop={onDrop}
                    onLayoutChange={onLayoutChange}
                    measureBeforeMount={false}
                    useCSSTransforms={state.mounted}
                    compactType={state.compactType}
                    preventCollision={!state.compactType}
                    isDroppable={true}
                  >
                    {state.items.map(el => {
                      if (el.startsWith('input')) {
                        return <div key={el} className="d-grid gap-2 react-resizable-hide">
                          <Form.Group as={Row} className="mb-3" controlId={el}>
                            <Form.Label column sm="3">
                              {el[0].toUpperCase()}{el.substring(1)}
                            </Form.Label>
                            <Col sm="9">
                              <Form.Control type="text" placeholder="" />
                            </Col>
                          </Form.Group>
                        </div>
                      } if (el.startsWith('password')) {
                        return <div key={el} className="d-grid gap-2 react-resizable-hide">
                          <Form.Group as={Row} className="mb-3" controlId={el}>
                            <Form.Label column sm="3">
                              {el[0].toUpperCase()}{el.substring(1)}
                            </Form.Label>
                            <Col sm="9">
                              <Form.Control type="password" placeholder="" />
                            </Col>
                          </Form.Group>
                        </div>
                      } else if (el.startsWith('select')) {
                        return <div key={el} className="d-grid gap-2 react-resizable-hide">
                          <Form.Group as={Row} className="mb-3" controlId={el}>
                            <Form.Label column sm="3">
                              {el[0].toUpperCase()}{el.substring(1)}
                            </Form.Label>
                            <Col sm="9">
                            <Form.Select aria-label="Default select example">
                              <option>Select ...</option>
                              <option value="1">value 1</option>
                              <option value="2">value 2</option>
                              <option value="3">value 3</option>
                            </Form.Select>
                            </Col>
                          </Form.Group>
                        </div>
                      } else {
                        return <div key={el} className="d-grid gap-2 react-resizable-hide">
                          <Button variant="primary" className="drag-handler">
                            {el[0].toUpperCase()}{el.substring(1)}
                          </Button>
                        </div>
                      }
                    })}
                  </ResponsiveGridLayout>
                </Form>
                </Accordion.Body>
              </Accordion.Item>
            </Accordion>
          </Col>
        </Row>
      </Container>
    </div>
  );
}

export default App;
```

运行效果如下图：

![运行效果][1711971786920]

[1711971786920]:data:image/png;base64,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