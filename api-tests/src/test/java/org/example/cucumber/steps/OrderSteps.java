package org.example.cucumber.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.example.base.AbstractTest;
import org.example.base.BaseDbTest;
import org.example.constants.ApiConstants;
import org.example.context.ScenarioContext;
import org.example.models.OrderRequestDto;
import org.example.models.OrderResponseDto;
import org.example.models.enums.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.example.utils.ApiTestUtils.*;

public class OrderSteps extends AbstractTest {

    @Autowired
    private ScenarioContext context;
    @Autowired
    private BaseDbTest orderDb;

    public OrderSteps() {
    }

    // WHEN

    @When("user tries to get all orders")
    public void user_sends_get_request_to_know_all_orders() {
        Response response = sendOrderRequest(ApiConstants.ALL_ORDERS, "GET", null);
        log.info("GET all orders response: {}", response.asString());
        context.set("response", response);
    }

    @When("user creates a new order with username {string}, description {string}, and amount {string}")
    public void user_creates_new_order_with_params(String username, String description, String amountStr) {
        OrderRequestDto requestOrder = buildOrderRequest(username, description, amountStr);
        log.info("POST create order request: {}", requestOrder);

        Response response = sendOrderRequest(ApiConstants.CREATE_ORDER, "POST", requestOrder);
        log.info("POST create order response: {}", response.asString());

        context.set("response", response);
        context.set("requestOrder", requestOrder);

        if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
            var createdOrder = response.as(OrderResponseDto.class);
            context.set("saved_id", createdOrder.getId());
            log.info("Order created with id: {}", createdOrder.getId());
        }
    }

    @When("user tries to get order by id {string}")
    public void user_tries_to_get_order_by_id(String idStr) {
        Long id = resolveId(idStr, context);
        log.info("GET order by id: {}", id);

        Response response = sendOrderRequest(ApiConstants.ORDER_BY_ID.replace("{id}", id.toString()), "GET", null);
        log.info("GET order by id response: {}", response.asString());
        context.set("response", response);
    }

    @When("user updates the order with id {string} and username {string}, description {string}, and amount {double}")
    public void user_sends_put_request_to_update_order(String idStr, String username, String description, Double amount) {
        Long id = resolveId(idStr, context);
        OrderRequestDto requestOrder = buildOrderRequest(username, description, amount != null ? amount.toString() : null);

        log.info("PUT update order id: {}, request: {}", id, requestOrder);

        Response response = sendOrderRequest(ApiConstants.ORDER_BY_ID.replace("{id}", id.toString()), "PUT", requestOrder);
        log.info("PUT update order response: {}", response.asString());
        context.set("response", response);
    }

    @When("user partially updates the fields of the order with id {string}")
    public void user_partially_updates_fields_of_order_with_any_id(String idStr, DataTable dataTable) {
        Long id = resolveId(idStr, context);
        var updates = dataTable.asMap(String.class, Object.class);
        log.info("PATCH order id: {}, updates: {}", id, updates);

        Response response = sendOrderRequest(ApiConstants.ORDER_BY_ID.replace("{id}", id.toString()), "PATCH", updates);
        log.info("PATCH order response: {}", response.asString());
        context.set("response", response);
    }

    @When("user deletes the order with id {string}")
    public void user_sends_delete_request_to_delete_order(String idStr) {
        Long id = resolveId(idStr, context);
        log.info("DELETE order id: {}", id);

        Response response = sendOrderRequest(ApiConstants.ORDER_BY_ID.replace("{id}", id.toString()), "DELETE", null);
        log.info("DELETE order response: {}", response.asString());
        context.set("response", response);
    }

    @When("user hard deletes the order with id {string}")
    public void user_sends_delete_request_to_hard_delete_order(String idStr) {
        Long id = resolveId(idStr, context);
        log.info("HARD DELETE order id: {}", id);

        Response response = sendOrderRequest(ApiConstants.HARD_DELETE_ORDER.replace("{id}", id.toString()), "DELETE", null);
        log.info("HARD DELETE order response: {}", response.asString());
        context.set("response", response);
    }

    @When("database table {string} is cleared")
    public void db_table_is_cleared(String tableName) {
        try {
            orderDb.clearTable(tableName);
            await()
                    .atMost(5, TimeUnit.SECONDS)
                    .pollInterval(200, TimeUnit.MILLISECONDS)
                    .until(() -> orderDb.countInDbTable(tableName) == 0);
            log.info("Rows in table {} after clear: {}", tableName, orderDb.countInDbTable(tableName));
        } catch (Exception e) {
            log.error("Failed to clear table: {}", tableName, e);
            throw e;
        }
    }

    // THEN

    @Then("the response should have status code {int}")
    public void the_response_should_have_status_code(int statusCode) {
        Response response = context.get("response", Response.class);
        Assertions.assertThat(response)
                .withFailMessage("No response found in scenario context")
                .isNotNull();
        log.info("Expected status code: {}, actual: {}", statusCode, response.getStatusCode());
        response.then().statusCode(statusCode);
    }

    @Then("^the response should( not)? contain at least one order$")
    public void the_response_should_contain_at_least_one_order(String not) {
        Response response = context.get("response", Response.class);
        Assertions.assertThat(response)
                .withFailMessage("No response found in scenario context")
                .isNotNull();
        var orders = response.jsonPath().getList("$", OrderResponseDto.class);
        log.info("Orders found: {}", orders);

        if (not != null && not.trim().equals("not")) {
            Assertions.assertThat(orders).isEmpty();
        } else {
            Assertions.assertThat(orders).isNotEmpty();
        }
    }

    @Then("the response should contain the order with username {string}, status {orderStatus}, description {string}, and amount {double}")
    public void the_response_should_contain_order_with_params(String username, OrderStatus status, String
            description, Double amount) {
        Response response = context.get("response", Response.class);
        Assertions.assertThat(response)
                .withFailMessage("No response found in scenario context")
                .isNotNull();
        var order = response.as(OrderResponseDto.class);
        log.info("Asserting order: {}", order);
        Assertions.assertThat(order.getUsername()).isEqualTo(username);
        Assertions.assertThat(order.getStatus()).isEqualTo(status);
        Assertions.assertThat(order.getDescription()).isEqualTo(description);
        Assertions.assertThat(order.getAmount()).isEqualTo(amount);
    }

    @Then("the response should contain the order with id {string}")
    public void the_response_should_contain_order_with_id(String idStr) {
        Long id = resolveId(idStr, context);
        Response response = context.get("response", Response.class);
        Assertions.assertThat(response)
                .withFailMessage("No response found in scenario context")
                .isNotNull();
        var order = response.as(OrderResponseDto.class);
        log.info("Asserting order by id: {}, order: {}", id, order);
        Assertions.assertThat(order.getId()).isEqualTo(id);
    }

    @Then("no order should have negative amount")
    public void no_order_should_have_negative_amount() {
        Response response = context.get("response", Response.class);
        Assertions.assertThat(response)
                .withFailMessage("No response found in scenario context")
                .isNotNull();
        var orders = response.jsonPath().getList("$", OrderResponseDto.class);
        boolean noNegative = orders.stream().noneMatch(o -> o.getAmount() < 0);
        log.info("No negative amount: {}", noNegative);
        Assertions.assertThat(noNegative).isTrue();
    }

    @Then("the response should contain error message {string}")
    public void the_response_should_contain_error_message(String expectedMessage) {
        Response response = context.get("response", Response.class);
        Assertions.assertThat(response)
                .withFailMessage("No response found in scenario context")
                .isNotNull();
        var errorMessage = response.jsonPath().getString("amount");
        log.info("Asserting error message: expected '{}', actual '{}'", expectedMessage, errorMessage);
        Assertions.assertThat(errorMessage).isEqualTo(expectedMessage);
    }
}