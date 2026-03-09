package org.example.cucumber.hooks;

import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import com.github.tomakehurst.wiremock.WireMockServer;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class WiremockHooks {
    private static WireMockServer wireMockServer;

    @Before(order = 0)
    public void startWireMock(Scenario scenario) {
        if (scenario.getSourceTagNames().contains("@mock")) {
            if (wireMockServer == null || !wireMockServer.isRunning()) {
                wireMockServer = new WireMockServer(
                        wireMockConfig()
                                .port(8082)
                                .usingFilesUnderDirectory("src/test/resources/wiremock")
                );
                wireMockServer.start();
                System.out.println("WireMock started on port 8082");
            }
        }
    }

    @After(order = 100)
    public void stopWireMock(Scenario scenario) {
        if (scenario.getSourceTagNames().contains("@mock")) {
            if (wireMockServer != null && wireMockServer.isRunning()) {
                wireMockServer.stop();
                System.out.println("WireMock stopped");
            }
        }
    }
}