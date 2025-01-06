<%--
  Created by IntelliJ IDEA.
  User: amirkurmaev
  Date: 05.01.2025
  Time: 23:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <meta charset="UTF-8">
  <title>CRM Service. Спецификации</title>
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
    <h1>Спецификация</h1>
    <form action="/crm/specification/add" method="post">
      <label for="deviceType">Тип устройства: </label><br>
      <select name="deviceType" id="deviceType" required onchange="sendTypeDevice(this.value)">
        <option value="" disabled selected>Тип устройства</option>
        <c:forEach var="typeDevice" items="${typeDevices}">
          <option value="${typeDevice.id}">${typeDevice.name}</option>
        </c:forEach>
      </select><br>

      <label for="manufacturer">Производитель: </label><br>
      <div id="manufacturer-create">
        <select name="manufacturer" id="manufacturer" required>
          <option value="" disabled selected>Производитель</option>
        </select>
        <button type="button" onclick="renderManufacturerCreate()">Добавить нового производителя</button>
        <div id="manufacturer-button">
          <button type="button" onclick="renderManufacturerLink()">Привязать тип устройства</button>
        </div>
      </div>

      <label for="model">Модель: </label>
      <input type="text" name="model" id="model" required>

      <label for="article">Артикул: </label>
      <input type="text" name="article" id="article" required>

      <c:if test="${not empty error}">
        <p class="warn">${error}</p>
      </c:if>

      <button type="submit">Добавить</button>
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

    function renderManufacturerCreate() {
        let innerHtml = '<input type="text" name="manufacturer" id="manufacturer-input" required>';
        innerHtml += '<button type="button" onclick="checkAndSendManufacturer()">Добавить производителя</button>';
        $('#manufacturer-create').html(innerHtml);
    }

    function renderManufacturer() {
        let innerHtml = '<select name="manufacturer" id="manufacturer" required onchange="sendTypeManufacturer(this.value, $("' + "#deviceType" + '").val())">';
        innerHtml += '</select><br>'
        $('#manufacturer-create').html(innerHtml);
    }

    function sendManufacturer(manufacturerName, deviceType) {
        let data = {
            "manufacturerName": manufacturerName,
            "deviceType": deviceType
        };

        $.ajax({
            type: "POST",
            url: "/crm/manufacturer/add",
            data: JSON.stringify(data),
            success: function (response) {
                renderManufacturer();
                sendTypeDevice(deviceType);
            },
            dataType: "json",
            contentType: "application/json"
        });
    }

    function checkAndSendManufacturer() {
        const deviceTypeValue = $('#deviceType').val();
        const manufacturerValue = $('#manufacturer-input').val();

        if (!deviceTypeValue) {
            alert('Поле "Тип устройства" должно быть заполнено.')
            return;
        }

        if (!manufacturerValue) {
            alert('Поле "Производитель" должно быть заполнено.')
            return;
        }

        sendManufacturer(manufacturerValue, deviceTypeValue);
    }

    function renderSuccess(div) {
        let innerHtml = "<p class=warn>Тип устройства успешно привязан с производителем</p>"
        div.html(innerHtml);
    }

    function checkAndSendTypeDeviceAndManufacturer() {
        const deviceTypeValue = $('#deviceType').val();
        const manufacturerValue = $('#manufacturer').val();

        if (!deviceTypeValue) {
            alert('Поле "Тип устройства" должно быть заполнено.')
            return;
        }

        if (!manufacturerValue) {
            alert('Поле "Производитель" должно быть заполнено.')
            return;
        }
        renderSuccess($('#manufacturer-button'));
        sendManufacturerBinding(manufacturerValue, deviceTypeValue);
    }

    function sendManufacturerBinding(manufacturerId, deviceTypeId) {
        let data = {
            "manufacturerId": manufacturerId,
            "deviceTypeId": deviceTypeId
        };

        $.ajax({
            type: "POST",
            url: "/crm/specification/binding/manufacturer",
            data: JSON.stringify(data),
            success: function (response) {
                sendTypeDevice(deviceTypeId);
            },
            dataType: "json",
            contentType: "application/json"
        });
    }

    function renderManufacturerButton() {
        let innerHtml = '<button type="button" onclick="checkAndSendTypeDeviceAndManufacturer()">Привязать с типом устройства</button>'
        $('#manufacturer-button').html(innerHtml);
    }

    function renderManufacturerLink() {
        $.ajax({
            type: "GET",
            url: "/crm/specification/get/manufacturers",
            success: function (response) {
                renderManufacturerSelect(response, $('#manufacturer'));
                renderManufacturerButton();
            },
            dataType: "json",
            contentType: "application/json"
        });
    }
</script>
</body>
</html>
