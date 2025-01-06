<%--
  Created by IntelliJ IDEA.
  User: amirkurmaev
  Date: 05.01.2025
  Time: 12:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <meta charset="UTF-8">
  <title>CRM Service. Сотрудники</title>
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
    <h1>Сотрудники</h1>
    <c:if test="${error != null}">
      <p class="warn">${error}</p>
    </c:if>
    <form action="/crm/employee/list" method="get">
      <input type="text" name="name" placeholder="Имя" required>
      <input type="text" name="surname" placeholder="Фамилия" required>
      <input type="text" name="patronymic" placeholder="Отчество (не обязательное поле)">
      <button type="submit">Найти</button>
      <button type="button" onclick="window.location.href='/crm/employee/register'">Зарегистрировать</button>
    </form>
  </div>
  <div class="data-list">
    <table>
      <thead>
      <tr>
        <th>Имя</th>
        <th>Фамилия</th>
        <th>Отчество</th>
        <th>Контактный номер</th>
        <th>Электронная почта</th>
        <th>Дата трудоустройства</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach var="employee" items="${employees}">
        <tr onclick="window.location.href='/crm/employee/?id=${employee.email}';">
          <td>${employee.name}</td>
          <td>${employee.surname}</td>
          <td>${employee.patronymic}</td>
          <td>${employee.phoneNumber}</td>
          <td>${employee.email}</td>
          <td>${employee.dateOfEmployment}</td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </div>
</main>
</body>
</html>
