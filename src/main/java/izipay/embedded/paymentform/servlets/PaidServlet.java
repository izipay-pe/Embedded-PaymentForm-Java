import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebServlet(name = "PaidServlet", urlPatterns = { "/paid" })
public class PaidServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        Map<String, String[]> parameterMap = request.getParameterMap();
        
        String krAnswer = null;
        if (parameterMap.containsKey("kr-answer")) {
            krAnswer = request.getParameter("kr-answer");
        }

        if (krAnswer != null) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonResponse = mapper.readTree(krAnswer);

            String orderTotalAmount = jsonResponse.path("orderDetails").get("orderTotalAmount").asText();
            String paymentStatus = jsonResponse.path("transactions").get(0).get("status").asText();
            String transactionId = jsonResponse.path("transactions").get(0).get("uuid").asText();

            request.setAttribute("orderTotalAmount", orderTotalAmount);
            request.setAttribute("paymentStatus", paymentStatus);
            request.setAttribute("transactionId", transactionId);

            request.getRequestDispatcher("/respuesta.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing kr-answer parameter");
        }
    }
}

