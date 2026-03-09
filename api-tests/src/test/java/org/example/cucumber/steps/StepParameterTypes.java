package org.example.cucumber.steps;

import io.cucumber.java.ParameterType;
import org.example.models.enums.OrderStatus;

public class StepParameterTypes {
    @ParameterType("CREATED|UPDATED|PARTIALLY_UPDATED|DELETED")
    public OrderStatus orderStatus(String status) {
        return OrderStatus.valueOf(status);
    }
}