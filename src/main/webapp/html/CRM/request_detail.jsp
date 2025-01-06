<%--
  Created by IntelliJ IDEA.
  User: amirkurmaev
  Date: 04.01.2025
  Time: 21:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <meta charset="UTF-8">
  <title>CRM. Заявка. Описание</title>
  <link rel="stylesheet" href="/static/css/colors.css">
  <link rel="stylesheet" href="/static/css/base.css">
  <link rel="stylesheet" href="/static/css/header.css">
  <link rel="stylesheet" href="/static/css/detail.css">
  <link rel="stylesheet" href="/static/css/form.css">
  <link rel="stylesheet" href="/static/css/list.css">
</head>
<body>
<header>
  <jsp:include page="../includes/header.jsp"/>
</header>
<main>
  <div class="card">
    <div class="card-header">Заявка ${request.identifier}</div>
    <div class="card-content">
      <h3>Подробная информация</h3>
    </div>
    <div class="info-group">
      <strong>Описание: </strong> <span>${request.description}</span>
    </div>

    <div class="info-group">
      <strong>Дата создания: </strong> <span>${request.date}</span>
    </div>

    <div class="info-group">
      <strong>Статус:</strong> <span>${request.status}</span>
    </div>

    <div class="info-group">
      <strong>ФИО клиента:</strong>
      <span>${request.clientSurname} ${request.clientName} ${request.clientPatronymic}</span>
    </div>

    <div class="info-group">
      <strong>Контактный номер:</strong> <span>${request.clientPhone}</span>
    </div>

    <div class="info-group">
      <strong>Электронная почта:</strong> <span>${request.clientEmail}</span>
    </div>

    <div class="info-group">
      <strong>Производитель:</strong> <span>${request.manufacturer}</span>
    </div>

    <div class="info-group">
      <strong>Модель:</strong> <span>${request.model}</span>
    </div>

    <div class="info-group">
      <strong>Артикул:</strong> <span>${request.article}</span>
    </div>

    <div class="info-group">
      <strong>Серийный номер:</strong> <span>${request.serialNumber}</span>
    </div>

    <div class="info-group">
      <strong>Цвет:</strong> <span>${request.color}</span>
    </div>

    <div class="info-group">
      <strong>Состояние в момент поступления: </strong> <span><a href="/uploaded/files?id=${request.file}">Файл</a></span>
    </div>
    <div class="user-form">
      <form action="/crm/request/?id=${request.identifier}" method="post">
        <input type="hidden" name="identifier" value="${request.identifier}">
        <label for="status">Изменить статус заявки: </label>
        <select name="status" id="status">
          <c:forEach var="status" items="${statuses}">
            <option value="${status.state}" <c:if test="${request.status.equals(status.state)}">selected</c:if>>${status.state}</option>>
          </c:forEach>
        </select><br>
        <button type="submit">Сохранить статус</button>
        <br>
      </form>
      <form action="/crm/request/?id=${request.identifier}" method="post">
        <input type="hidden" name="identifier" value="${request.identifier}">
        <label for="work">Добавить услугу: </label>
        <select id="work" name="newWork">
          <option value="" selected disabled>Услуги</option>
          <c:forEach var="newWork" items="${newWorks}">
            <option value="${newWork.id}">${newWork.typeWork}</option>
          </c:forEach>
        </select>
        <button type="submit">Добавить услугу</button>
      </form>
    </div>
    <div class="data-list" id="works">
      <table>
        <thead>
        <tr>
          <th>Тип работы</th>
          <th>Цена</th>
          <th>Гарантия</th>
          <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="work" items="${works}">
          <tr>
            <form action="/crm/request/?id=${request.identifier}" method="post">
              <input type="hidden" name="identifier" value="${request.identifier}">
              <td>${work.typeWork}</td>
              <td>${work.price}</td>
              <td>${work.warranty}</td>
              <td>
                <input type="hidden" name="workId" value="${work.id}">
                <button type="submit">Удалить</button>
              </td>
            </form>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </div>
  </div>
</main>
</body>
</html>
