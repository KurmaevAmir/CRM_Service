<%--
  Created by IntelliJ IDEA.
  User: amirkurmaev
  Date: 04.01.2025
  Time: 16:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <meta charset="UTF-8">
  <title>CRM Service. Добавление устройства</title>
  <link rel="stylesheet" href="/static/css/colors.css">
  <link rel="stylesheet" href="/static/css/base.css">
  <link rel="stylesheet" href="/static/css/header.css">
  <link rel="stylesheet" href="/static/css/form.css">
  <script
          src="https://code.jquery.com/jquery-3.5.1.js"
          integrity="sha256-QWo7LDvxbWT2tbbQ97B53yJnYU3WhH/C8ycbRAkjPDc="
          crossorigin="anonymous"></script>
</head>
<body>
<header>
  <jsp:include page="../includes/header.jsp"/>
</header>
<main>
  <div class="user-form">
    <h1>Добавление устройства</h1>
    <form method="post" action="/crm/device/create">
      <label for="deviceType">Устройство: </label><br>
      <select name="deviceType" id="deviceType" required onchange="sendTypeDevice(this.value)">
        <option value="" disabled selected>Тип устройства</option>
        <c:forEach var="typeDevice" items="${typeDevices}">
          <option value="${typeDevice.id}">${typeDevice.name}</option>
        </c:forEach>
      </select><br>

      <select name="manufacturer" id="manufacturer" required onchange="sendTypeManufacturer(this.value,
  $('#deviceType').val())">
        <option value="" disabled selected>Производитель</option>
      </select><br>

      <select name="specification" id="specification" required>
        <option value="" disabled selected>Модель</option>
      </select><br>

      <label for="serialNumber">Серийный номер: </label>
      <input type="text" name="serialNumber" id="serialNumber" maxlength="12" required>

      <label for="color">Цвет: </label>
      <input type="text" name="color" id="color" required>

      <button type="submit">Создать</button>
    </form>
  </div>
</main>
<script>
    function renderManufacturerSelect(manufacturers, select) {
        let innerHtml = '<option value="" disabled selected>Производитель</option>';
        for (let i = 0; i < manufacturers.length; i++) {
            innerHtml += '<option value="' + manufacturers[i]['id'] + '">' + manufacturers[i]['name'] + '</option>';
        }
        select.html(innerHtml);
    }

    function sendTypeDevice(id) {
        let data = {
            "id": id
        };

        $.ajax({
            type: "GET",
            url: "/crm/request/get/manufacturer",
            data: data,
            success: function (response) {
                renderManufacturerSelect(response, $('#manufacturer'));
            },
            dataType: "json",
            contentType: "application/json"
        });
    }

    function renderSpecificationSelect(specifications, select) {
        let innerHtml = '<option value="" disabled selected>Модель</option>';
        for (let i = 0; i < specifications.length; i++) {
            innerHtml += '<option value="' + specifications[i]['id'] + '">' + specifications[i]['model'] + ' ' + specifications[i]['article'] + '</option>';
        }
        select.html(innerHtml);
    }

    function sendTypeManufacturer(manufacturerId, typeDeviceId) {
        let data = {
            "manufacturerId": manufacturerId,
            "typeDeviceId": typeDeviceId
        };
        $.ajax({
            type: "GET",
            url: "/crm/request/get/specification",
            data: data,
            success: function (response) {
                renderSpecificationSelect(response, $('#specification'));
            },
            dataType: "json",
            contentType: "application/json"
        });
    }
</script>
</body>
</html>
