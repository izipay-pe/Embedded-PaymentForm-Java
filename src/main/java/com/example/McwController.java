package com.example;

import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;
import java.io.UnsupportedEncodingException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.binary.Hex;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;


public class McwController {
    
    private McwProperties properties = new McwProperties();

    // Método para generar un orderNumber basado en la hora
    public String generarOrderId() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("'Order-'yyyyMMddHHmmss");
        return LocalDateTime.now().format(formatter);
    }


    // Genera el FormToken para el despliegue de la pasarela en web
    public String generarToken(Map<String, String> parameters) {
        

	// Obteniendo claves API
	String merchantCode = properties.getProperty("merchantCode");
	String password =  properties.getProperty("password");
        String publicKey = properties.getProperty("publicKey");
	String hmacKey = properties.getProperty("hmacKey");
	String formToken = "";

	// Definiendo valores para la estructura del Json
	//// Crear el cuerpo de la solicitud JSON
        JSONObject billingDetails = new JSONObject();
        billingDetails.put("firstName", parameters.get("firstName"));
        billingDetails.put("lastName", parameters.get("lastName"));
        billingDetails.put("phoneNumber", parameters.get("phoneNumber"));
        billingDetails.put("identityType", parameters.get("identityType"));
        billingDetails.put("identityCode", parameters.get("identityCode"));
        billingDetails.put("address", parameters.get("address"));
        billingDetails.put("country", parameters.get("country"));
        billingDetails.put("state", parameters.get("state"));
        billingDetails.put("city", parameters.get("city"));
        billingDetails.put("zipCode", parameters.get("zipCode"));
	
	JSONObject customer = new JSONObject();
        customer.put("email", parameters.get("email"));
        customer.put("billingDetails", billingDetails);

	String amountStr = parameters.get("amount");
	BigDecimal amountUnit = new BigDecimal(amountStr);
	BigDecimal amountCent = amountUnit.multiply(BigDecimal.valueOf(100));
	long amount = amountCent.longValue();

        JSONObject requestBody = new JSONObject();
        requestBody.put("amount", amount);
        requestBody.put("currency", parameters.get("currency"));
        requestBody.put("customer", customer);
        requestBody.put("orderId", parameters.get("orderId"));
	
	// Creando la Conexión
	try {
	   // Encabezado Basic con concatenación de "usuario:contraseña" en base64
	   String encoded = Base64.getEncoder().encodeToString((merchantCode+":"+password).getBytes(StandardCharsets.UTF_8));

           // Crear la conexión a la API para la creación del FormToken
	   URL url = new URL("https://api.micuentaweb.pe/api-payment/V4/Charge/CreatePayment");
           HttpURLConnection conn = (HttpURLConnection) url.openConnection();
           conn.setRequestMethod("POST");
	   conn.setRequestProperty("Authorization", "Basic "+encoded);
           conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
           conn.setDoOutput(true);

	   // Realiza la solicitud POST
	   try (OutputStream os = conn.getOutputStream()) {
        	byte[] input = requestBody.toString().getBytes(StandardCharsets.UTF_8); // Codifica explícitamente como UTF-8
        	os.write(input, 0, input.length);
    		}

            // Leer la respuesta de la API
            try (BufferedReader bf = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;

                StringBuilder response = new StringBuilder();

                while ((line = bf.readLine()) != null) {
                    response.append(line);
                }
		
		// Obtenemos el FormToken generado
		JSONObject jsonResponse = new JSONObject(response.toString()); 
		formToken = jsonResponse.getJSONObject("answer").getString("formToken");


            }

        } catch (Exception e) {
            	e.printStackTrace();
        }
	
	return formToken;
    	}
   
    
    // Genera un hash HMAC-SHA256
    public String HmacSha256(String data, String key) {
    	try {
        	Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        	SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        	sha256_HMAC.init(secret_key);
        	return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
    	} catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException e) {
        	e.printStackTrace();
        	throw new RuntimeException("Error generando HMAC SHA256", e);
    	}
    }


    // Verifica la integridad del Hash recibido y el generado  	
    public boolean checkHash(String krHash, String krHashKey, String krAnswer){
	String passwordKey = properties.getProperty("password");
	String hmacSha256Key = properties.getProperty("hmacKey");
	String key;
	
	// Verifica si la respuesta es de 'Retorno a la tienda' o de la 'IPN'
	if ("sha256_hmac".equals(krHashKey)){
		key = hmacSha256Key;
	} else if ("password".equals(krHashKey)) {
        	key = passwordKey;
        } else {	
		return false;
        }
       	
	// Calculamos un Hash usando el valor del 'kr-answer' y el valor del 'kr-hash-key'
	String calculatedHash = HmacSha256(krAnswer, key);
	// Comparamos si el hash es igual y retornamos la respuesta
	return calculatedHash.equals(krHash);

    }
}
