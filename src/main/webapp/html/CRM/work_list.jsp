<%--
  Created by IntelliJ IDEA.
  User: amirkurmaev
  Date: 05.01.2025
  Time: 20:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <meta charset="UTF-8">
  <title>CRM Service. Спецификации</title>
  <link rel="stylesheet" href="/static/css/colors.css">
  <link rel="stylesheet" href="/static/css/base.css">
  <link rel="stylesheet" href="/static/css/header.css">
  <link rel="stylesheet" href="/static/css/form.css">
  <link rel="stylesheet" href="/static/css/list.css">
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
    <h1>Услуги</h1>
    <form action="/crm/work/list" method="get">

      <label for="deviceType">Спецификации: </label><br>
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

      <button type="submit">Найти</button>
      <button type="button" onclick="window.location.href='/crm/work/add'">Добавить</button>
    </form>
  </div>
  <div class="data-list">
    <table>
      <thead>
      <tr>
        <th>Тип работы</th>
        <th>Цена</th>
        <th>Гарантия</th>
        <th>Производитель</th>
        <th>Модель</th>
        <th>Серийный номер</th>
        <th></th>
      </tr>
      </thead>
      <tbody>
      <c:forEach var="work" items="${works}">
        <tr>
          <form action="/crm/work/list" method="post">
            <td>${work.typeWork}</td>
            <td>${work.price}</td>
            <td>${work.warranty}</td>
            <td>${work.manufacturer}</td>
            <td>${work.model}</td>
            <td>${work.article}</td>
            <td>
              <input type="hidden" name="workId" value="${work.id}">
              <button type="submit" class="irrevocable">Удалить</button>
            </td>
          </form>
        </tr>
      </c:forEach>
      </tbody>
    </table>
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

    function renderDeviceDiv(device, div) {
        let innerHtml
        if (device == null) {
            innerHtml += '<p>УСТРОЙСТВО НЕ НАЙДЕНО</p>'
        } else {
            innerHtml = '<p>Серийный номер: ' + device['serialNumber'] + '</p>';
            innerHtml += '<p>Цвет устройства: ' + device['color'] + '</p>';
        }
        div.html(innerHtml);
    }
</script>
</body>
</html>
