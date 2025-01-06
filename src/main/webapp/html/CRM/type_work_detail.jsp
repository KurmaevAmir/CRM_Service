<%--
  Created by IntelliJ IDEA.
  User: amirkurmaev
  Date: 06.01.2025
  Time: 18:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <meta charset="UTF-8">
  <title>CRM Service. Типы работ</title>
  <link rel="stylesheet" href="/static/css/colors.css">
  <link rel="stylesheet" href="/static/css/base.css">
  <link rel="stylesheet" href="/static/css/header.css">
  <link rel="stylesheet" href="/static/css/form.css">
  <link rel="stylesheet" href="/static/css/list.css">
</head>
<body>
<header>
  <jsp:include page="../includes/header.jsp"/>
</header>
<div class="card">
  <div class="user-form">
    <h1>Тип работы</h1>
    <form action="/crm/type/work/?id=${typeWork.id}" method="post">
      <input type="text" name="typeWorkOperation" value="${typeWork.operation}">
      <button type="submit">Сохранить</button>
    </form>
  </div>
</div>
</body>
</html>
