package config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

@Configuration
@PropertySources({
        @PropertySource(value = "classpath:${environment:stage}.properties")}
)
@ComponentScan(basePackages = {"api"})
public class ApplicationContext {
    private final Logger log = LoggerFactory.getLogger(ApplicationContext.class);

    @Autowired
    public Environment env;

    public static void main(String args[]){
        System.out.println("Starting api automation framework configuration");
    }
    public static PropertySourcesPlaceholderConfigurer propertyConfigIn() {
        return new PropertySourcesPlaceholderConfigurer();
    }
    @PostConstruct
    public void log() {
        log.info("Automation config loaded successfully!");  // Displays as expected
        log.info("env: {}", env);                // env: null
    }
}
