package org.example.cucumber.hooks;
import org.example.utils.RestAssuredConfig;
import io.cucumber.java.Before;
import org.example.context.ScenarioContext;

public class CommonCucumberHooks {
    public static final ThreadLocal<ScenarioContext> context = new ThreadLocal<>();

    @Before(order = 1)
    public void setup() {
        RestAssuredConfig.setup();
        context.set(new ScenarioContext());
    }
}