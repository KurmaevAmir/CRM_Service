<%--
  Created by IntelliJ IDEA.
  User: amirkurmaev
  Date: 28.12.2024
  Time: 09:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <meta charset="UTF-8">
  <title>CRM Service</title>
  <link rel="stylesheet" href="/static/css/colors.css">
  <link rel="stylesheet" href="/static/css/base.css">
  <link rel="stylesheet" href="/static/css/header.css">
  <link rel="stylesheet" href="/static/css/form.css">
</head>
<body>
<header>
  <jsp:include page="../includes/header.jsp"/>
</header>
<main>
  <div class="user-form">
    <h1>Панель администратора</h1>
    <form action="/crm/request/list" method="get">
      <label for="status">Отобразить заявки со статусом:</label>
      <select name="status" id="status">
        <option value="Принято" <c:if test="${status == \"Принято\"}">selected</c:if>>Принято</option>
        <option value="В обработке" <c:if test="${status == \"В обработке\"}">selected</c:if>>В обработке</option>
        <option value="Требует согласования" <c:if test="${status == \"Требует согласования\"}">selected</c:if>>Требует
          согласования
        </option>
        <option value="Готово" <c:if test="${status == \"Готово\"}">selected</c:if>>Готово</option>
      </select>
      <input type="submit" value="Применить">
    </form>

    <table>
      <thead>
      <tr>
        <th>Описание</th>
        <th>Дата создания</th>
        <th>Статус</th>
        <th>Устройство</th>
        <th>Клиент</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach var="request" items="${requests}">
        <tr>
          <td>${request.description}</td>
          <td>${request.dateCreation}</td>
          <td>${request.status}</td>
          <td>${request.device}</td>
          <td>${request.client}</td>
          <td>
            <form action="/crm/change_status" method="post">
              <input type="hidden" name="requestId" value="${request.id}">
              <select name="newStatus">
                <option value="Принято" <c:if test="${request.status == \"Принято\"}">selected</c:if>>Принято</option>
                <option value="В обработке" <c:if test="${request.status == \"В обработке\"}">selected</c:if>>В
                  обработке
                </option>
                <option value="Требует согласования"
                        <c:if test="${request.status == \"Требует согласования\"}">selected</c:if>>Требует согласования
                </option>
                <option value="Готово" <c:if test="${request.status == \"Готово\"}">selected</c:if>>Готово</option>
                <input type="submit" value="Сохранить">
              </select>
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
