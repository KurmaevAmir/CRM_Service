<%--
  Created by IntelliJ IDEA.
  User: amirkurmaev
  Date: 28.12.2024
  Time: 09:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="/static/css/colors.css">
  <link type="text/css" rel="stylesheet" href="<c:url value="/static/css/base.css"/>">
  <link type="text/css" rel="stylesheet" href="<c:url value="/static/css/form.css"/>">
  <title>Авторизация</title>
</head>
<body>
<main>
  <div class="user-form">
    <form action="/login" method="post">
      <h1>Авторизация</h1>
      <input name="email" type="email" placeholder="Почта" required>
      <input name="password" type="password" placeholder="Пароль" required>
      <c:if test="${not empty error}">
        <p class="warn">${error}</p>
      </c:if>
      <button type="submit">Авторизоваться</button>
    </form>
  </div>
</main>
</body>
</html>
