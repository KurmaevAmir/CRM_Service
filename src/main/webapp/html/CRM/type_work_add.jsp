<%--
  Created by IntelliJ IDEA.
  User: amirkurmaev
  Date: 06.01.2025
  Time: 20:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <meta charset="UTF-8">
  <title>CRM. Добавление типа работы</title>
  <link rel="stylesheet" href="/static/css/colors.css">
  <link rel="stylesheet" href="/static/css/base.css">
  <link rel="stylesheet" href="/static/css/header.css">
  <link rel="stylesheet" href="/static/css/form.css">
</head>
<body>
<header>
  <jsp:include page="../includes/header.jsp"/>
</header>
<main></main>
<div class="user-form">
  <h1>Добавление типа работы</h1>
  <form method="post" action="/crm/type/work/add">
    <label for="typeWork">Тип работы: </label>
    <input type="text" name="typeWork" id="typeWork" required>
    <button type="submit">Добавить</button>
  </form>
</div>
</body>
</html>
