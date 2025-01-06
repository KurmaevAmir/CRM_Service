<%--
  Created by IntelliJ IDEA.
  User: amirkurmaev
  Date: 28.12.2024
  Time: 09:54
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ru">
<head>
  <title>Service</title>
  <meta charset="UTF-8">
  <link rel="stylesheet" href="/static/css/colors.css">
  <link rel="stylesheet" href="/static/css/base.css">
  <link rel="stylesheet" href="/static/css/header.css">
  <link rel="stylesheet" href="/static/css/list.css">
</head>
<body>
<header>
  <jsp:include page="includes/header.jsp"/>
</header>
<main>
  <div class="data-list">
    <table>
      <thead>
      <tr>
        <th>Идентификатор</th>
        <th>Производитель</th>
        <th>Модель</th>
        <th>Серийный номер</th>
        <th>Статус</th>
        <th>Дата создания заявки</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach var="request" items="${requests}">
        <c:if test="${userRole.equals('employee')}">
          <tr onclick="window.location.href='/crm/request/?id=${request.identifier}';">
        </c:if>
        <c:if test="${userRole.equals('client')}">
          <tr onclick="window.location.href='/detail?id=${request.identifier}';">
        </c:if>
          <td>${request.identifier}</td>
          <td>${request.manufacturer}</td>
          <td>${request.model}</td>
          <td>${request.serialNumber}</td>
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
