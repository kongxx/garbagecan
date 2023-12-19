<%@ page language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
    <script type="text/javascript" src="<c:url value="/resources/js/jquery-2.1.1.js" />"></script>
    <title>Spring4 MVC - Home</title>
    <script type="text/javascript">
    $(document).ready(function() {
        $("a.writeJsonLink").click(function() {
            var link = $(this);
            $.ajax({ url: this.href,
                beforeSend: function(req) {
                    if (!this.url.match(/\.json$/)) {
                        req.setRequestHeader("Accept", "application/json");
                    }
                },
                success: function(json) {
                    alert(json);
                    //MvcUtil.showSuccessResponse(JSON.stringify(json), link);
                },
                error: function(xhr) {
                    alert(xhr.responseText);
                    //MvcUtil.showErrorResponse(xhr.responseText, link);
                }});
            return false;
        });
        $("a.writeXmlLink").click(function() {
            var link = $(this);
            $.ajax({ url: link.attr("href"),
                beforeSend: function(req) {
                    if (!this.url.match(/\.xml$/)) {
                        req.setRequestHeader("Accept", "application/xml");
                    }
                },
                success: function(xml) {
                    alert(xml);
                    //MvcUtil.showSuccessResponse(MvcUtil.xmlencode(xml), link);
                },
                error: function(xhr) {
                    //MvcUtil.showErrorResponse(xhr.responseText, link);
                }
            });
            return false;
        });
    });

    </script>
    </head>

    <body>
        <h1>Home</h1>

        <table border="0"  width="100%">
            <tr>
                <td width="40%">
                    <h2>Hello</h2>
                    <ul>
                        <li><a href="/springweb/hello/" target="myframe">Hello World</a></li>
                        <li><a href="/springweb/hello/?name=Kongxx" target="myframe">Hello Kongxx</a></li>
                    </ul>
                </td>

                <td width="60%">
                    <iframe id="myframe" name="myframe" width="500px" height="300px"></iframe>
                </td>
            </tr>
        </table>
    </body>
</html>