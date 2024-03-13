/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package izipay.embedded.paymentform.servlets;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import izipay.embedded.paymentform.logica.formToken;
import izipay.embedded.paymentform.logica.formulariotoken;
import java.io.InputStream;
import java.util.Base64;
import java.util.Properties;
import net.minidev.json.JSONArray;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;


/**
 *
 * @author junio
 */
@WebServlet(name = "Svformtoken", urlPatterns = { "/Svformtoken" })
public class Svformtoken extends HttpServlet {
    
    private String siteId;
    private String keyTest;
 
      protected void processRequest(HttpServletRequest request, HttpServletResponse response)
                  throws ServletException, IOException {
      }

      // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the
      // + sign on the left to edit the code.">
      /**
       * Handles the HTTP <code>GET</code> method.
       *
       * @param request  servlet request
       * @param response servlet response
       * @throws ServletException if a servlet-specific error occurs
       * @throws IOException      if an I/O error occurs
       */
      @Override
      protected void doGet(HttpServletRequest request, HttpServletResponse response)
                  throws ServletException, IOException {

      }

      /**
       * Handles the HTTP <code>POST</code> method.
       *
       * @param request  servlet request
       * @param response servlet response
       * @throws ServletException if a servlet-specific error occurs
       * @throws IOException      if an I/O error occurs
       */
      @Override
      protected void doPost(HttpServletRequest request, HttpServletResponse response)
                  throws ServletException, IOException {
            
            String monto = request.getParameter("Monto");
            String moneda = request.getParameter("Moneda");

            try {
                // Obtener la ruta del recurso
                String resourcePath = "/WEB-INF/Config/credenciales.properties";
                InputStream input = getServletContext().getResourceAsStream(resourcePath);
                
                // Verificar si el recurso se encuentra
                if (input == null) {
                    System.out.println("Sorry, unable to find " + resourcePath);             
                    return;
                }
                
                Properties prop = new Properties();
                prop.load(input);
                siteId = prop.getProperty("site_id");
                keyTest = prop.getProperty("key_test");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            
            String authString = Base64.getEncoder().encodeToString(String.format("%s:%s", siteId,keyTest).getBytes());

            int montocadena = Integer.parseInt(monto);
            String montoreal = Integer.toString(montocadena * 100);

            System.out.println("mi monto es: " + monto);
            System.out.println("mi moneda es: " + moneda);
            System.out.println("mi Id-2 es: " + siteId);
            System.out.println("mi keyTest-2 es: " + keyTest);            
            System.out.println("mi authString es: " + authString);
            
            

            String tokenres;
            List<formulariotoken> enviarformulario = new ArrayList<>();

            // Esta variable res la usaremos únicamente para dar un respuesta final

            String URL = "https://api.micuentaweb.pe/api-payment/V4/Charge/CreatePayment";

            // Creamos el cliente de conexión al API Restful
            Client client = ClientBuilder.newClient();

            // Creamos el target lo cuál es nuestra URL junto con el nombre del método a
            // llamar
            WebTarget target = client.target(URL);

            // Creamos nuestra solicitud que realizará el request
            Invocation.Builder solicitud = target.request();
            MultivaluedMap<String, Object> myHeaders = new MultivaluedHashMap<>();
            myHeaders.add("Authorization", "Basic "+ authString);// autenticacion es la clave en base64 necesaria para la autorización con el API
            myHeaders.add("Content-Type", "application/x-www-form-urlencoded");
            myHeaders.add("Accept", "application/json");
            myHeaders.add("grant_type", "client_credentials");
            solicitud.headers(myHeaders);

            // Creamos y llenamos nuestro objeto BaseReq con los datos que solicita el API
            formToken req = new formToken();
            req.setAmount(montoreal);
            req.setCurrency("PEN");

            // Convertimos el objeto req a un json
            Gson gson = new Gson();
            String jsonString = gson.toJson(req);
            System.out.println("Data para enviar es: " + jsonString);

            // Enviamos nuestro json vía post al API Restful

            Response post = solicitud.post(Entity.json(jsonString));

            // System.out.println("Data para enviar esgdfg: "+post);
            // Recibimos la respuesta y la leemos en una clase de tipo String, en caso de
            // que el json sea tipo json y no string, debemos usar la clase de tipo
            // JsonObject.class en lugar de String.class
            String responseJson = post.readEntity(String.class);
            // res = responseJson;

            // Imprimimos el status de la solicitud
            System.out.println("Respuesta http Status: " + post.getStatus());

            switch (post.getStatus()) {
                  case 200:
                        // System.out.println(responseJson);

                        ObjectMapper mapper = new ObjectMapper();
                        try {
                              JsonNode node = mapper.readTree(responseJson);
                              tokenres = node.get("answer").get("formToken").asText();
                              String statusres = node.get("status").asText();
                              // System.out.println("node"+node);
                              System.out.println(
                                          "FormToken status: " + statusres + "\n" + "FormToken: " + tokenres + "\n");
                              enviarformulario.add(new formulariotoken(tokenres));

                        } catch (JsonProcessingException e) {
                        }

                        break;

                  default:
                        String res = "Error";
                        break;
            }
            HttpSession misesion = request.getSession();
            misesion.setAttribute("tokenmostrar", enviarformulario);
            response.sendRedirect("FormularioIncrustado.jsp");
      }

      /**
       * Returns a short description of the servlet.
       *
       * @return a String containing servlet description
       */
      @Override
      public String getServletInfo() {
            return "Short description";
      }// </editor-fold>

      @SuppressWarnings("unused")
      private JSONArray JSONArray(StringBuilder data) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from
                                                                           // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
      }

}

