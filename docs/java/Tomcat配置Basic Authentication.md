# Tomcat配置Basic Authentication

## 创建Web App

首先准备一个Tomcat环境，这里使用的Tomcat7.x。

创建一个简单的web app用来测试，这里假定使用myapp。

## 添加依赖库

因为需要获取用户登录的用户名和密码，所以使用了apache的commons-codec库来解码，可以从apache的网站上下载commons-codec-1.10.jar包，并放到myapp/WEB-INF/lib目录下。

## 配置用户/密码/角色

修改Tomcat的conf目录下的tomcat-users.xml文件，内容如下：

``` xml
<?xml version='1.0' encoding='utf-8'?>
<tomcat-users>
  <role rolename="tomcat"/>
  <role rolename="manager"/>
  <user username="tomcat" password="tomcat" roles="tomcat"/>
  <user username="manager" password="manager" roles="manager"/>
</tomcat-users>

```

## 配置web

修改myapp/WEB-INF/web.xml文件

``` xml
<?xml version="1.0" encoding="ISO-8859-1"?>
<web-app xmlns="http://java.sun.com/xml/ns/javaee"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://java.sun.com/xml/ns/javaee
                      http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
  version="3.0" metadata-complete="true">

    <description>myapp</description>
    <display-name>myapp</display-name>
	
	<security-constraint>
		<display-name>Security Constraint</display-name>
		<web-resource-collection>
			<web-resource-name>Protected Area</web-resource-name>
			<url-pattern>/*</url-pattern>
			<http-method>DELETE</http-method>
			<http-method>GET</http-method>
			<http-method>POST</http-method>
			<http-method>PUT</http-method>
		</web-resource-collection>
		<auth-constraint>
			<role-name>tomcat</role-name>
			<role-name>manager</role-name>
		</auth-constraint>
	</security-constraint>

	<login-config>
		<auth-method>BASIC</auth-method>
	</login-config>
	
	<security-role>
      <role-name>tomcat</role-name>
    </security-role>
	<security-role>
      <role-name>manager</role-name>
    </security-role>
	
    <welcome-file-list>
        <welcome-file>index.jsp</welcome-file>
    </welcome-file-list>

</web-app>

```

## 测试页面

添加一个测试页面index.jsp，内容如下：

``` jsp
<%@page language="java" import="java.util.*" %>
<%@page language="java" import="org.apache.commons.codec.binary.Base64" %>

<%
	Enumeration headerNames = request.getHeaderNames();
	while (headerNames.hasMoreElements()) {
		String headerName = (String) headerNames.nextElement();
		String headerValue = request.getHeader(headerName);
		out.println(headerName + ": " + headerValue + "<br/>");
	}
	
	out.println("<hr/>");
	
	String authHeader = request.getHeader("authorization");
	String encodedValue = authHeader.split(" ")[1];
	out.println(new String(Base64.decodeBase64(encodedValue)));

%>
```

## 测试

使用浏览器访问http://localhost:8080/myapp/，此时会弹出一个窗口提示输入用户名和密码，使用tomcat/tomcat或者manager/manager进行登录，登录成功后页面显示如下内容，最下方是解码后的用户名和密码。

``` html
accept: image/gif, image/jpeg, image/pjpeg, application/x-ms-application, application/xaml+xml, application/x-ms-xbap, */*
accept-language: zh-Hans-CN,zh-Hans;q=0.8,en-US;q=0.5,en;q=0.3
user-agent: Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 10.0; WOW64; Trident/7.0; .NET4.0C; .NET4.0E; .NET CLR 2.0.50727; .NET CLR 3.0.30729; .NET CLR 3.5.30729; InfoPath.3)
accept-encoding: gzip, deflate
host: 192.168.0.41:8080
connection: Keep-Alive
cookie: JSESSIONID=6AE4989CC03AD16468D2686A717FAE97; jhsessionId=EE8026D614FD04F8CC825E35230DE7CB
authorization: Basic dG9tY2F0OnRvbWNhdA==

--------------------------------------------------------------------------------
tomcat:tomcat 
```





