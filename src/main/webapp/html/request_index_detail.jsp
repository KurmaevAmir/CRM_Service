<%--
  Created by IntelliJ IDEA.
  User: amirkurmaev
  Date: 06.01.2025
  Time: 16:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <meta charset="UTF-8">
  <title>Описание</title>
  <link rel="stylesheet" href="/static/css/colors.css">
  <link rel="stylesheet" href="/static/css/base.css">
  <link rel="stylesheet" href="/static/css/header.css">
  <link rel="stylesheet" href="/static/css/detail.css">
  <link rel="stylesheet" href="/static/css/list.css">
</head>
<body>
<header>
  <jsp:include page="includes/header.jsp"/>
</header
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
</div>
</body>
</html>
