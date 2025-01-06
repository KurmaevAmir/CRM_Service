<%--
  Created by IntelliJ IDEA.
  User: amirkurmaev
  Date: 03.01.2025
  Time: 22:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="logo">
  <a href="/">Service</a>
</div>
<nav>
  <c:if test="${sessionScope.userRole == 'user'}">
    <a href="/requests">Мои заявки</a>
  </c:if>
  <c:if test="${sessionScope.userRole == 'employee'}">
    <a href="/crm/request/create">Создать заявку</a>
    <a href="/crm/request/list">Заявки</a>
    <a href="/crm/employee/list">Сотрудники</a>
    <a href="/crm/work/list">Услуги</a>
    <a href="/crm/manufacturer/list">Производители</a>
    <a href="/crm/specification/list">Спецификации</a>
    <a href="/crm/type/work/list">Типы работ</a>
    <a href="/crm/type/device/list">Типы устройств</a>
  </c:if>
  <a href="/logout">Выйти</a>
</nav>