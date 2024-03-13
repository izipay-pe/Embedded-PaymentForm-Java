<%-- 
    Document   : FormularioIncrustado
    Created on : 27 feb. 2024, 20:15:59
    Author     : junio
--%>

<%@page import="izipay.embedded.paymentform.logica.formToken"%>
<%@page import="izipay.embedded.paymentform.logica.formulariotoken"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta name="viewport" 
   content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta http-equiv="X-UA-Compatible" content="IE=edge" /> 

  <!-- Javascript library. Should be loaded in head section -->
  <script 
   src="https://api.micuentaweb.pe/static/js/krypton-client/V4.0/stable/kr-payment-form.min.js"
   kr-public-key="~~CHANGE_ME_PUBLIC_KEY~~"
   kr-post-url-success="paid.php">
  </script>

  <!-- theme and plugins. should be loaded after the javascript library -->
  <!-- not mandatory but helps to have a nice payment form out of the box -->
  <link type="text/css" rel="stylesheet" href="css/styles.css">
  <link rel="stylesheet" href="https://api.micuentaweb.pe/static/js/krypton-client/V4.0/ext/classic-reset.css">
  <script 
   src="https://api.micuentaweb.pe/static/js/krypton-client/V4.0/ext/classic.js">
  </script>
</head>
<body>
    
    <div class="formulario" >
        <img src="image/logo-250x102.png"/>
        <div class="py-5 text-center">      
            <h2>Ejemplo de un formulario incrustado con JAVA</h2>
            <p class="lead"></p>
        </div>
            <%  
                //String monto = request.getParameter("Monto");
                List<formulariotoken> listaFormulario = (List) request.getSession().getAttribute("tokenmostrar");        
                    for(formulariotoken token : listaFormulario){                    
            %>    
            <!-- payment form -->
            
            <div class="kr-embedded" kr-form-token=<%=token.getTokenFormulario()%>>

              <!-- payment form fields -->
              <div class="kr-pan"></div>
              <div class="kr-expiry"></div>
              <div class="kr-security-code"></div>  

              <!-- payment form submit button -->
              <button class="kr-payment-button"></button>

              <!-- error zone -->
              <div class="kr-form-error"></div>
            </div>            
            <% 
                }
            %>  
    </div>
</body>
</html>
