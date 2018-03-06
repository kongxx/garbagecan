<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <s:head />
</head>

<body>
    <h1>Struts2 file upload example</h1>
    <s:form action="doUpload" method="POST" enctype="multipart/form-data">
        <input id="user.name" name="user.name" value="hello" /><br/><br/>
        <s:file name="myFile" label="Select a File to upload" /><br/><br/>
        <s:submit value="submit" name="submit" />
    </s:form>
</body>
</html>