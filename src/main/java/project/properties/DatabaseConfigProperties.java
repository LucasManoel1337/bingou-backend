package project.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.datasource")
public class DatabaseConfigProperties {

    // RENOMEIE ESTE CAMPO
    private String url; // Antes era jdbcUrl
    private String username;
    private String password;
    private String driverClassName;

    // AJUSTE O GETTER E O SETTER CORRESPONDENTES
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // ... getters e setters restantes não mudam
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getDriverClassName() { return driverClassName; }
    public void setDriverClassName(String driverClassName) { this.driverClassName = driverClassName; }
}