<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:ui="http://java.sun.com/jsf/facelets"
      xmlns:f="http://java.sun.com/jsf/core"
      xmlns:h="http://java.sun.com/jsf/html"
      xmlns:p="http://primefaces.org/ui">
      
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/css/general.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/css/fontStyles.css">
	<link rel="stylesheet" type="text/css" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">	
	<script type="text/javascript" src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
	<script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/gsap/1.16.1/TweenMax.min.js"></script>
	<title>Inicio de Sesión</title>
	<script src="https://www.google.com/recaptcha/api.js"></script>
</head>

<body class="login">

	<div style="position: absolute; top: 3vw; left: 2vw;width:100%">
		<div>
			<h1 style="font-size: 2.5vw; margin-bottom: 5px;">UNIVERSIDAD CENTRAL DEL ECUADOR</h1>
		</div>
		
		<div>
			<h1 style="font-size: 2vw;">SISTEMA DE TITULACION DE POSGRADOS</h1>
		</div>
	</div>

	<div id="login-button">
		<i class="fa fa-user"></i>
	</div>

	<div id="container" style="height: 348px;">
		<h1 style="color: #000;margin: 40px auto auto;">INICIAR SESIÓN</h1>
		<span class="close-btn"> 
			<img src="https://cdn4.iconfinder.com/data/icons/miu/22/circle_close_delete_-128.png"></img>
		</span>

		<form action="<%=request.getContextPath()%>/j_spring_security_check" method="post">
			<div class="usuario">
				<i class="fa fa-user-o"></i>
				<input type="text" id="j_username" name="j_username" placeholder="Usuario"/>
			</div>
			<div class="password">
				<i class="fa fa-key"></i>
				<input type="password" id="j_password" name="j_password" placeholder="Contraseña"/>
			</div>
			<div style="display: flex;justify-content: center;width: 103%;" class="g-recaptcha" data-sitekey="6LfVWbkUAAAAAGaX4na9cH32wXqgdxSUWykP8MCk"></div>
			<div class="ingresar">
				<input type="submit" value="Ingresar"/>
			</div>
			<div id="remember-container">	
						 
				<a href="<%=request.getContextPath()%>/titulacionPosgrado/generalidades/registroPostulante/registroPostulante.jsf">
					<h1 id="remember">REGISTRARSE</h1>
				</a>
				
				<a href="<%=request.getContextPath()%>/titulacionPosgrado/generalidades/recuperaClave/recuperaClave.jsf">
					<h1 id="forgotten">RECUPERAR CONTRASEÑA</h1>
				</a>
			</div>
		</form>
	</div>
	
	<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/theme.js"></script>
</body>

</html>

