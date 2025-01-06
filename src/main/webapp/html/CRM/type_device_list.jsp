<%--
  Created by IntelliJ IDEA.
  User: amirkurmaev
  Date: 06.01.2025
  Time: 22:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <meta charset="UTF-8">
  <title>CRM Service. Типы устройств</title>
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
    <button type="button" onclick="window.location.href='/crm/type/device/add';">Добавить новый тип устройств</button>
  </div>
  <div class="data-list">
    <table>
      <thead>
      <tr>
        <th>Тип девайса</th>
        <th></th>
      </tr>
      </thead>
      <tbody>
      <c:forEach items="${typesDevice}" var="typeDevice">
        <tr>
          <td>${typeDevice.name}</td>
          <td>
            <form action="/crm/type/device/list" method="post">
              <input type="hidden" name="typeDeviceId" value="${typeDevice.id}">
              <button type="submit">Удалить</button>
            </form>
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </div>
</main>
</body>
</html>
