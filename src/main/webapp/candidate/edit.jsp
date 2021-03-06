<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="ru.job4j.dreamjob.model.Candidate" %>
<%@ page import="ru.job4j.dreamjob.store.db.CandidatePsqlStore" %>
<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
            integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n"
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
            integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
            crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
            integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6"
            crossorigin="anonymous"></script>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

    <title>Работа мечты</title>
</head>
<body>
<%
    String id = request.getParameter("id");
    Candidate candidate = new Candidate(0, "", "");
    if (id != null) {
        candidate = CandidatePsqlStore.getStore().findById(Integer.parseInt(id));
    }
%>
<div class="container pt-3">
    <div class="row">
        <ul class="nav">
            <li class="nav-item">
                <a class="nav-link" href='<c:url value="/posts.do" />'>Вакансии</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href='<c:url value="/candidates.do" />'>Кандидаты</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href='<c:url value="/posts.do?edit=true" />'>Добавить вакансию</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href='<c:url value="/candidates.do?edit=true" />'>Добавить кандидата</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="#">${user.name}</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href='<c:url value="/auth.do?exit=true" />'>Выйти</a>
            </li>
        </ul>
    </div>
    <div class="row">
        <div class="card" style="width: 100%">
            <div class="card-header">
                <% if (id == null) { %>
                    Новый кандидат.
                <% } else { %>
                    Редактирование кандидата.
                <% } %>
            </div>
            <div class="card-body">
                <form class="row" action="${pageContext.request.contextPath}/candidates.do?id=<%=candidate.getId()%>" method="post" enctype="multipart/form-data">
                    <div class="col-md-3">
                        <% if (candidate.getPhoto() == null) { %>
                            <img style="width:100%;" src="${pageContext.request.contextPath}/static/default.jpg" alt="img-fluid">
                            <input type="file" name="photo" class="my-2"  alt="Photo" onchange="previewFile()">
                        <% } else { %>
                            <img style="width:100%;" src="${pageContext.request.contextPath}/download?name=<%=candidate.getPhoto().getName()%>" alt="img-fluid">
                            <input type="file" name="photo" class="my-2"  alt="Photo" onchange="previewFile()">
                        <% } %>
                    </div>
                    <div class="col-md-9">
                        <div class="form-group">
                            <label>Имя</label>
                            <input type="text" class="form-control" name="name" value="<%=candidate.getName()%>">
                        </div>
                        <div class="form-group">
                            <label>О себе</label>
                            <input type="text" class="form-control" name="memo" value="<%=candidate.getMemo()%>">
                        </div>
                        <div class="form-group">
                            <label>Город</label>
                            <% if (candidate.getCityId() == null) { %>
                                    <p id="city-id" hidden>0</p>
                            <% } else { %>
                                    <p id="city-id" hidden><%=candidate.getCityId()%></p>
                            <% } %>
                            <select id="city-control" name="city" class="form-control" required>

                            </select>
                        </div>
                        <div class="form-group row">
                            <button type="submit" class="btn btn-primary col-md-5 mx-2">Сохранить</button>
                            <a href="${pageContext.request.contextPath}/candidates.do" class="btn btn-danger col-md-5">Отменить</a>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<script>

    $(document).ready(function () {
        console.log("document ready!");
        getCities();

        function getCities() {
            $.ajax({
                type: 'GET',
                url: 'http://localhost:8080/dreamjob/city',
                dataType: 'json'
            }).done(function(data) {
                let count = data.length;
                console.log("Cities successfully load! Count = " + count);
                let cityID = $('#city-id').text();
                console.log("City id = " + cityID);
                for (let i = 0; i < count; i++) {
                    let city = data[i];
                    console.log(city.id + " " + city.name);
                    let option = new Option(city.name, city.id, cityID == city.id, cityID == city.id);
                    $(option).html(city.name);
                    $('#city-control').append(option);
                }
            }).fail(function(err){
                console.log("Something went wrong!");
            });
        }

    });

    function previewFile(){

        var preview = document.querySelector('img'); //selects the query named img
        var previousSource = preview.getAttribute("src");
        var file    = document.querySelector('input[type=file]').files[0]; //sames as here
        var reader  = new FileReader();

        reader.onloadend = function () {
            preview.src = reader.result;
        };

        if (file) {
            reader.readAsDataURL(file); //reads the data as a URL
        } else {
            preview.src = previousSource;
        }
    }

</script>

</body>
</html>