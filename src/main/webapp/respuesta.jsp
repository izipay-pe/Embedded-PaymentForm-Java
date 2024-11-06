<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Formulario Incrustado - Izipay</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-GLhlTQ8iRABdZLl6O3oVMWSktQOp6b7In1Zl3/Jr59b6EGGoI1aFkw7cmDA6j6gD" crossorigin="anonymous">
	<link type="text/css" rel="stylesheet" href="css/styles.css">
    </head>

<body>

   <main class="main" id="top">
    <div class="container-fluid" data-layout="container">
            <div class="content">
                <nav class="nav-bar">
                  <a href="/">
                    <img
                      src="https://iziweb001b.s3.amazonaws.com/webresources/img/logo.png"
                      width="150"
                      alt=""
                  /></a>
                </nav>
                <section class="container" >
    			<div class="row">
      				<div class="col mx-auto">
        				<h1 style="color: white">Resultado de pago</h1>

    					<p><strong>Total de la orden:</strong> ${orderTotalAmount} ${currency}</p>
    					<p><strong>Estado del pago:</strong> ${paymentStatus}</p>
    					<p><strong>ID de la transacción:</strong> ${transactionId}</p>
          				<form action="/Embedded-PaymentForm-Java-1.0/" method="get">
            				<button class="btn btn-izi">
                				Volver a probar
            				</button>
          				</form>
      				</div>
    			</div>
  		</section>
            </div>
    </div>
</main>


</body>
</html>

