<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <title>CRM. Регистрация сотрудника</title>
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
    <h1>Регистрация сотрудника</h1>
    <c:if test="${not empty errors}">
      <ul>
        <c:forEach items="${errors}" var="error">
          <li>${error}</li>
        </c:forEach>
      </ul>
    </c:if>

    <form action="/crm/employee/register" method="post">
      <label for="name">Имя</label><br>
      <input type="text" name="name" id="name" required><br><br>

      <label for="surname">Фамилия</label><br>
      <input type="text" name="surname" id="surname" required><br><br>

      <label for="patronymic">Отчество</label><br>
      <input type="text" name="patronymic" id="patronymic"><br><br>

      <label for="date_of_birth">Дата рождения</label><br>
      <input type="date" name="date_of_birth" id="date_of_birth" required><br><br>

      <label for="phoneNumber">Номер телефона</label><br>
      <input type="tel" name="phoneNumber" id="phoneNumber"
             pattern="^\+?\d{10,15}$" required><br><br>

      <label for="email">Почта</label><br>
      <input type="email" name="email" id="email" required><br><br>

      <label for="passportSeries">Серия паспорта</label><br>
      <input type="text" name="passportSeries" id="passportSeries"
             pattern="\d{4}" required><br><br>

      <label for="passportNumber">Номер паспорта</label><br>
      <input type="text" name="passportNumber" id="passportNumber"
             pattern="\d{6}" required><br><br>

      <label for="passportIssueDate">Выдан</label><br>
      <input type="date" name="passportIssueDate" id="passportIssueDate" required><br><br>

      <label for="passportIssued">Кем выдан</label><br>
      <input type="text" name="passportIssued" id="passportIssued" required><br><br>

      <label for="passportSubdivision">Подразделение</label><br>
      <input type="text" name="passportSubdivision" id="passportSubdivision"
             pattern="\d{6}" required><br><br>

      <label for="password">Пароль</label><br>
      <input type="password" name="password" id="password" required><br><br>

      <label for="confirmPassword">Подтверждение пароля</label><br>
      <input type="password" name="confirmPassword" id="confirmPassword" required><br><br>

      <label for="snils">СНИЛС</label><br>
      <input type="text" name="snils" id="snils" pattern="\d{11}" required><br><br>

      <label for="inn">ИНН</label>
      <input type="text" name="inn" id="inn" pattern="\d{12}" required><br><br>

      <button type="submit">Зарегистрировать</button>
    </form>
  </div>
</main>
</body>
</html>