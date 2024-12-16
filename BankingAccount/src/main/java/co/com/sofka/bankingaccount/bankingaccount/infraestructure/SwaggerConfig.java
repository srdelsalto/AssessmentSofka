package co.com.sofka.bankingaccount.bankingaccount.infraestructure;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI BankingAccountAPI(){
        return new OpenAPI().info(new Info().title("Sofka Technologies - Assessment 5")
                .description("SpringBoot App with MongoDB for Sofka Technologies")
                .version("1.0")
                .license(new License().name("Apache 2.0").url("https://sofka.com.co/")));
    }
}
