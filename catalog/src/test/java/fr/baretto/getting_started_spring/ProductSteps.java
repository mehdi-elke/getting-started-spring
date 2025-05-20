package fr.baretto.getting_started_spring;

import fr.baretto.getting_started_spring.data.request.ProductRequestDto;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty"},
        features = "src/test/resources/com/yourcompany/app",
        glue = "fr.baretto.getting_started_spring"
)
public class ProductSteps {

    @Autowired
    private TestRestTemplate restTemplate;

    private ProductRequestDto productToCreate;
    private ResponseEntity<String> response;

    @Given("a productRequestDto with name {string}, description {string}, and price {double}")
    public void createProductDto(String name, String description, Double price) {
        productToCreate = new ProductRequestDto(name, description, price);
    }

    @When("we send POST {string} with this productRequestDto")
    public void postProduct(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ProductRequestDto> request = new HttpEntity<>(productToCreate, headers);
        response = restTemplate.postForEntity(url, request, String.class);
    }

    @Then("we receive a product with name {string}")
    public void verifyProductName(String expectedName) {
        assertThat(response.getBody()).contains(expectedName);
    }
}