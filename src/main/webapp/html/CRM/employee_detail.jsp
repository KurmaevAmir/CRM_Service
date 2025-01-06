<%--
  Created by IntelliJ IDEA.
  User: amirkurmaev
  Date: 05.01.2025
  Time: 15:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <meta charset="UTF-8">
  <title>Title</title>
  <link rel="stylesheet" href="/static/css/colors.css">
  <link rel="stylesheet" href="/static/css/base.css">
  <link rel="stylesheet" href="/static/css/header.css">
  <link rel="stylesheet" href="/static/css/detail.css">
  <link rel="stylesheet" href="/static/css/form.css">
</head>
<body>
<header>
  <jsp:include page="../includes/header.jsp"/>
</header>
<main>
  <div class="card">
    <div class="card-header">Сотрудник ${employee.surname} ${employee.name} ${employee.patronymic}</div>
    <div class="card-content">
      <h3>Подробная информация</h3>
    </div>
    <div class="card-group">
      <strong>Дата рождения: </strong> <span>${employee.dateBirth}</span>
    </div>

    <div class="card-group">
      <strong>Контактный номер: </strong> <span>${employee.phoneNumber}</span>
    </div>

    <div class="card-group">
      <strong>Электронная почта: </strong> <span>${employee.email}</span>
    </div>

    <div class="card-group">
      <strong>Дата трудоустройства: </strong> <span>${employee.dateEmployment}</span>
    </div>

    <div class="card-group">
      <strong>Серия и номер паспорта: </strong> <span>${employee.passportSeries} ${employee.passportNumber}</span>
    </div>

    <div class="card-group">
      <strong>Дата выдачи, кем выдан: </strong> <span>${employee.passportIssueDate}, ${employee.passportIssued}</span>
    </div>

    <div class="card-group">
      <strong>Подразделение: </strong> <span>${employee.passportSubdivision}</span>
    </div>

    <div class="card-group">
      <strong>Снилс: </strong> <span>${employee.snils}</span>
    </div>

    <div class="card-group">
      <strong>ИНН: </strong> <span>${employee.inn}</span>
    </div>
    <div class="user-form">
      <form action="/crm/employee/?id=${employee.email}" method="post">
        <button class="irrevocable" type="submit" ${employee.email == "rootadmin@localhost.ru" ? 'disabled' : ''}>Уволить</button>
      </form>
    </div>
  </div>
</main>
</body>
</html>
