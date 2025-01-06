<%--
  Created by IntelliJ IDEA.
  User: amirkurmaev
  Date: 06.01.2025
  Time: 17:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <meta charset="UTF-8">
  <title>CRM Service. Типы работ</title>
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
    <button type="button" onclick="window.location.href='/crm/type/work/add'">Добавить новый тип работы</button>
  </div>
  <div class="data-list">
    <table>
      <thead>
      <tr>
        <th>Тип работы</th>
        <th></th>
      </tr>
      </thead>
      <tbody>
      <c:forEach items="${typesWork}" var="typeWork">
        <tr onclick="window.location.href='/crm/type/work/?id=${typeWork.id}'">
          <form action="/crm/type/work/list" method="post">
            <td>${typeWork.operation}</td>
            <td>
              <input type="hidden" name="typeWorkId" value="${typeWork.id}">
              <button type="submit">Удалить</button>
            </td>
          </form>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </div>
</main>
</body>
</html>
