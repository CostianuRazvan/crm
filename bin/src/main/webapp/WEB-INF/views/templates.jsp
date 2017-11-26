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
	window.menu = 'templates';
</script>



<title>Templates-ReplyTechnology</title>

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

		<div class="container" id="thetable">
			<h2>ReplyTechnology Templates</h2>

			<table class="table table-striped table-borderd">
				<thead>
					<tr>
						<th>Template Name</th>
						<th>Delete</th>

					</tr>
				</thead>

				<tbody>

					<c:forEach items="${templates}" var="templates">
						<tr>
							<td>${templates.name}</td>
							<td>

								<button class="DeleteButton" type="submit" id=${templates.id} onClick="deleteTemplate(this.id);">Delete</button></td>
						</tr>
					</c:forEach>

				</tbody>
			</table>


			<h4>Insert a .docx file to add a new Template</h4>
			<script language="JavaScript" type="text/javascript">
				
								function deleteTemplate(id) {

								

														var urlToSend = 'http://localhost:8080/crm/deleteTemplate/'
																+ id;
														console.log(urlToSend);
														var req = new XMLHttpRequest();
														req
																.open(
																		"GET",
																		urlToSend,
																		true);
														req.responseType = "";
														req.onload = function(
																event) {
														};

														req.send();
				 
													};


			</script>

			<div>
				<form method="POST" enctype="multipart/form-data"
					action="${contextRoot}/addTemplate">
					
					<h6>Template name: <input type="text" name="name" style="margin-right:1em;width:200px" /></h6>
					<h6>File template: <input type="file" name="file" style="margin-left:18px;width:200px" /></h6>
					 <input type="submit"
						value="Add" />
					
				</form>
			</div>





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
