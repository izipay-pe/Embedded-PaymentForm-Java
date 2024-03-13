<%-- 
    Document   : index
    Created on : 27 feb. 2024, 20:16:33
    Author     : junio
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Formulario Incrustado - Izipay</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-GLhlTQ8iRABdZLl6O3oVMWSktQOp6b7In1Zl3/Jr59b6EGGoI1aFkw7cmDA6j6gD" crossorigin="anonymous">
    </head>
    <body class="bg-light">    
    
        <div class="container">
            <main>
                <div class="py-5 text-center">      
                <h2>Ejemplo de un formulario incrustado con JAVA</h2>
                <p class="lead"></p>
                </div>

                <div class="row g-8 d-flex justify-content-center">           

                    <div class="col-md-7 col-lg-8"> 
                        
                        <form action="Svformtoken" method="POST">
                            <div class="row g-3">
                                <div class="col-sm-6">
                                    <label>Monto Total</label>
                                    <input name="Monto" type="text" class="form-control" placeholder="S/." value=""  required>
                                    <div class="invalid-feedback">Ingrese un monto para pagar.</div>
                                </div>

                                <div class="col-sm-6">
                                    <label>Moneda</label>
                                    <input name="Moneda" type="text" class="form-control" placeholder="PEN" value="PEN" readonly="readonly">                    
                                </div>
                            </div>            

                            <hr class="my-4">

                            <button class="w-100 btn btn-danger btn-lg" type="submit">Finalizar con el Pago</button>
                        </form>
                        
                    </div>

                    <hr class="my-4">                  

                </div>
            </main>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js" integrity="sha384-w76AqPfDkMBDXo30jS1Sgez6pr3x5MlQ1ZAGC+nuZB+EYdgRZgiwxhTBTkF7CXvN" crossorigin="anonymous"></script>  
    </body>
</html>
