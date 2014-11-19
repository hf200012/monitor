<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>系统启动成功验证页面</title>

</head>
<body>
<h2>系统启动成功</h2>
<h2>
<%
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date = new Date();
    response.getWriter().println(format.format(date));
%>
</h2>
</body>
</html>
