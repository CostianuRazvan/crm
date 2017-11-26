<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url var="css" value="/resources/css" />
<spring:url var="images" value="/resources/images" />
<spring:url var="js" value="/resources/js" />


<c:set var="contextRoot" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>

<html lang="en">



<head>



<meta charset="utf-8">

<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">

<meta name="description" content="">

<meta name="author" content="">

<!-- setting menu variable for setting active tab menu -->
<script>
	window.menu = 'employees';
</script>



<title>Employees-ReplyTechnology</title>

<!-- Imports for table -->


<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>

<!-- Bootstrap core CSS -->

<link href="${css}/bootstrap.min.css" rel="stylesheet">



<!-- Custom styles for this template -->

<link href="${css}/shop-homepage.css" rel="stylesheet">



</head>



<body>



	<!-- Navigation -->

	<%@include file="./shared/navbar.jsp"%>

	<!-- Page Content -->
<body>
	<div class="wrapper">

		<div class="container">
			<h2>ReplyTechnology Employees</h2>

			<table id="thetable" class="table table-striped table-borderd">
				<thead>
					<tr>
						<th>Firstname</th>
						<th>Lastname</th>
						<th>CNP</th>
						<th>Function</th>
						<th>Choose an option</th>

					</tr>
				</thead>

				<tbody>

					<c:forEach items="${employees}" var="employee">
						<tr>
							<td>${employee.firstName}</td>
							<td>${employee.lastName}</td>
							<td>${employee.CNP}</td>
							<td>${employee.function}</td>
							<td><select id="templateSelector">
									<c:forEach items="${templates}" var="templates">
										<option value=${templates.name}>${templates.name}</option>
									</c:forEach>

							</select>

								<button class="GenerateButton" type="submit" id=${employee.id
									} onClick="generateTemplate(this.id);">Generate</button></td>
						</tr>
					</c:forEach>

				</tbody>
			</table>


			<h4>Insert a .xlsx file to update list of employees</h4>


			<div>
				<form method="POST" enctype="multipart/form-data"
					action="${contextRoot}/updateEmployees">
					<h6>File to upload:</h6>
					<input type="file" name="file" /> <input type="submit"
						value="Upload" />
					</table>
				</form>
			</div>

			<script language="JavaScript" type="text/javascript">
				function generateTemplate(id, contextRoot) {

					var e = document.getElementById("templateSelector");
					var fileTitle = e.options[e.selectedIndex].text;
					var urlToSend = 'http://localhost:8080/crm/generateTemplate/'
							+ fileTitle + "/" + id;
					var req = new XMLHttpRequest();
					req.open("GET", urlToSend, true);
					req.responseType = "blob";
					req.onload = function(event) {
						var blob = req.response;
						var fileName = req.getResponseHeader("FileName") //if you have the fileName header available
						var link = document.createElement('a');
						link.href = window.URL.createObjectURL(blob);
						link.download = fileName;
						link.click();
					};

					req.send();

				};
			</script>



		</div>
	</div>

</body>

<!-- /.container -->



<!-- Footer -->

<%@include file="./shared/footer.jsp"%>

<!-- Bootstrap core JavaScript -->

<script src="${js}/jquery.min.js"></script>
<script src="${js}/bootstrap.bundle.min.js"></script>

<!-- Adding script to save active state -->

<script src="${js}/navbarSelector.js"></script>



</body>





</html>
