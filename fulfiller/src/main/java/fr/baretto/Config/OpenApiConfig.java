package fr.baretto.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Fulfiller API")
                .version("1.0")
                .description("API de gestion des commandes de fulfillment"))
            .tags(Arrays.asList(
                new Tag().name("FulfillmentOrder").description("Gestion des commandes de fulfillment"),
                new Tag().name("Warehouse").description("Gestion des entrepôts et de la préparation des commandes"),
                new Tag().name("Shipment").description("Gestion des expéditions et du suivi des colis"),
                new Tag().name("OrderItem").description("Gestion des items de commande")
            ));
    }
} 