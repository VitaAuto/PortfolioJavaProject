package org.example.utils;

import io.restassured.response.Response;
import io.restassured.RestAssured;
import org.example.models.OrderRequestDto;
import org.example.context.ScenarioContext;

public class ApiTestUtils {

    public static Long resolveId(String idStr, ScenarioContext context) {
        if ("saved".equalsIgnoreCase(idStr) || "<saved>".equals(idStr)) {
            return context.get("saved_id", Long.class);
        }
        return Long.valueOf(idStr);
    }

    public static OrderRequestDto buildOrderRequest(String username, String description, String amountStr) {
        OrderRequestDto dto = new OrderRequestDto();
        dto.setUsername(username);
        dto.setDescription(description);
        if (amountStr != null && !amountStr.isBlank()) {
            try {
                dto.setAmount(Double.valueOf(amountStr));
            } catch (NumberFormatException e) {
                dto.setAmount(null);
            }
        } else {
            dto.setAmount(null);
        }
        return dto;
    }

public static Response sendOrderRequest(String endpoint, String method, Object body) {
    var req = RestAssured.given().contentType("application/json");
    if (body != null) req.body(body);

    return switch (method.toUpperCase()) {
        case "POST" -> req.post(endpoint);
        case "PUT" -> req.put(endpoint);
        case "PATCH" -> req.patch(endpoint);
        case "DELETE" -> req.delete(endpoint);
        case "GET" -> req.get(endpoint);
        default -> throw new IllegalArgumentException("Unsupported method: " + method);
    };
}
}