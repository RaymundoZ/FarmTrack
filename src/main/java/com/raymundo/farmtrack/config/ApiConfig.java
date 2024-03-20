package com.raymundo.farmtrack.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "FarmTrack Api",
                description = "Api for the FarmTrack application", version = "1.0.0",
                contact = @Contact(
                        name = "RaymundoZ",
                        url = "https://github.com/RaymundoZ/FarmTrack"
                )
        )
)
public class ApiConfig {
}
