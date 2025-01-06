<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Создание запроса</title>
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
    <h1>Заявка</h1>
    <form action="/crm/request/create" method="post" enctype="multipart/form-data">
      <label for="description">Описание: </label><br>
      <textarea name="description" id="description" maxlength="255" required></textarea><br>

      <label>Данные клиента: </label>
      <input type="text" id="name" placeholder="Имя">
      <input type="text" id="surname" placeholder="Фамилия">
      <input type="text" id="patronymic" placeholder="Отчество">
      <div class="button-container">
        <button type="button" onclick="sendClient(
        $('#name').val(),
        $('#surname').val(),
        $('#patronymic').val())">Найти
        </button>
        <button type="button" onclick="openClientCreation()">Создать</button>
      </div>
      <br>
      <select id="clients" name="client" required>
        <option value="" disabled selected>Клиент</option>
      </select><br><br>

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

      <input type="text" name="serialNumber" id="serialNumber" placeholder="Серийный номер" maxlength="12" required><br>
      <div class="button-container">
        <button type="button" onclick="sendSerialNumber(
        $('#serialNumber').val(),
        $('#specification').val()
    )">Найти
        </button>
        <button type="button" onclick="openSpecificationCreation()">Создать</button>
      </div>
      <div id="device">

      </div>

      <label for="file">Состояние девайса</label>
      <input type="file" id="file" name="file">
      <button type="submit">Создать</button>
    </form>
  </div>
</main>
<script>
    function renderClientsSelect(clients, select) {
        let innerHtml = '<option value="" disabled selected>Клиент</option>';
        for (let i = 0; i < clients.length; i++) {
            innerHtml += '<option value="' + clients[i]['id'] + '">' + clients[i]['surname'] + ' ' + clients[i]['name'] +
                ' ' + clients[i]['patronymic'] + '</option>';
        }
        select.html(innerHtml);
    }

    function sendClient(name, surname, patronymic) {
        let data = {
            "name": name,
            "surname": surname,
            "patronymic": patronymic
        };

        $.ajax({
            type: "GET",
            url: "/crm/request/get/clients",
            data: data,
            success: function (response) {
                renderClientsSelect(response, $('#clients'));
            },
            dataType: "json",
            contentType: "application/json"
        });
    }

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

    function sendSerialNumber(serialNumber, specification) {
        let data = {
            'serialNumber': serialNumber,
            'specification': specification
        }

        $.ajax({
            type: "GET",
            url: "/crm/request/get/device",
            data: data,
            success: function (response) {
                renderDeviceDiv(response, $('#device'));
            },
            dataType: "json",
            contentType: "application/json"
        })
    }

    function openClientCreation() {
        window.open('/crm/client/new', '_blank');
    }

    function openSpecificationCreation() {
        window.open('/crm/device/create', '_blank');
    }
</script>
</body>
</html>