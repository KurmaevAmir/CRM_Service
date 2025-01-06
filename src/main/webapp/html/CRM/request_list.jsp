<%--
  Created by IntelliJ IDEA.
  User: amirkurmaev
  Date: 04.01.2025
  Time: 18:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <meta charset="UTF-8">
  <title>CRM Service. Заявки</title>
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
<main>
  <div class="user-form">
    <h1>Заявки</h1>
    <c:if test="${error != null}">
      <p class="warn">${error}</p>
    </c:if>
    <form action="/crm/request/list" method="get">
      <label for="status">Отобразить заявки со статусом: </label>
      <select name="status" id="status">
        <c:forEach var="stage" items="${statuses}">
          <option value="${stage.state}" <c:if test="${status.equals(stage.state)}">selected</c:if>>${stage.state}</option>>
        </c:forEach>
      </select>
      <button type="submit">Применить</button>
    </form>
    <form action="/crm/request/list" method="get">
      <label for="search">Идентификатор</label>
      <input type="text" name="search" id="search">
      <button type="submit">Найти</button>
    </form>
  </div>
  <div class="data-list">
    <table>
      <thead>
      <tr>
        <th>Идентификатор</th>
        <th>Производитель</th>
        <th>Модель</th>
        <th>Серийный номер</th>
        <th>Статус</th>
        <th>Клиент</th>
        <th>Дата создания заявки</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach var="request" items="${requests}">
        <tr onclick="window.location.href='/crm/request/?id=${request.identifier}';">
          <td>${request.identifier}</td>
          <td>${request.manufacturer}</td>
          <td>${request.model}</td>
          <td>${request.serialNumber}</td>
          <td>${request.clientSurname}</td>
          <td>${request.status}</td>
          <td>${request.date_creation}</td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </div>
</main>
</body>
</html>
