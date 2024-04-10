# 使用formio和react实现在线表单设计

formiojs 是一个开源的在线表单设计工具，今天看看怎样在 react js 中使用 formiojs。

1. 首先创建一个react工程

``` shell
npx create-react-app my-react-formio-app
```

2. 安装依赖

``` shell
cd my-react-formio-app
npm install @formio/react
npm install @formio/js
```

另外，考虑样式等问题，还安装了下面几个依赖包

``` shell
npm install bootstrap 
npm install react18-json-view
npm install react-bootstrap
npm install font-awesome
```

3. 创建组件

这里创建一个简单的页面组件，主要包括三部分，第一部分是表单设计器，第二部分是表单预览，第三部分是表单JSON数据。

在 src 下创建文件 MyBuilder.js，内容如下：

``` javascript
import { FormBuilder, Form } from "@formio/react";
import { useState } from "react";
import ReactJson from "react18-json-view";
import { Container, Row, Col, Button, Accordion } from "react-bootstrap";

const MyBuilder = () => {

  const [jsonSchema, setSchema] = useState({ components: [] });

  const onFormChange = (schema) => {
    setSchema({ ...schema, components: [...schema.components] });
  };

  const handleSubmit = (data) => {
    console.log(data);
  }

  return (
    <>
      <Container>
        <Row>
          <Col >
            <Accordion defaultActiveKey="0">
              <Accordion.Item eventKey="0">
                <Accordion.Header>Builder</Accordion.Header>
                <Accordion.Body>
                <FormBuilder form={jsonSchema} onChange={onFormChange} />
                </Accordion.Body>
              </Accordion.Item>
            </Accordion>
          </Col>
        </Row>
        <Row>
          <Col >
            <Accordion defaultActiveKey="0">
              <Accordion.Item eventKey="0">
                <Accordion.Header>Form</Accordion.Header>
                <Accordion.Body>
                <Form form={jsonSchema} onSubmit={handleSubmit} />
                </Accordion.Body>
              </Accordion.Item>
            </Accordion>
          </Col>
        </Row>
        <Row>
          <Col >
            <Accordion defaultActiveKey="0">
              <Accordion.Item eventKey="0">
                <Accordion.Header>JSON Schema</Accordion.Header>
                <Accordion.Body>
                <ReactJson src={jsonSchema} name={null} collapsed={true}></ReactJson>
                </Accordion.Body>
              </Accordion.Item>
            </Accordion>
          </Col>
        </Row>
      </Container>
    </>
  );
};

export default MyBuilder;
```

4. 导入并渲染自定义组建

修改 src/App.js 文件，内容如下：

``` javascript
import React from 'react';
import MyBuilder from './MyBuilder';

import 'font-awesome/css/font-awesome.min.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import '@formio/js/dist/formio.full.min.css';
import './App.css';

function App() {
  return (
    <div className="App">
      <MyBuilder />
    </div>
  );
}

export default App;
```

5. 启动应用

``` shell
npm start
```

6. 访问应用

访问 http://localhost:3000/ 即可看到效果。可以在页面的表单设计中拖动组件，然后在表单预览中查看效果。

参考：
https://github.com/formio/react
https://formio.github.io/react-app-starterkit/#/
