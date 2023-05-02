package edu.nuist.codehelper.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Configuration
@ConfigurationProperties(prefix = "importr")
@PropertySource("classpath:application-import.yml")
@Data
public class ImportPackConfig {
    private Map<String, String> importr = new HashMap<>();
}
