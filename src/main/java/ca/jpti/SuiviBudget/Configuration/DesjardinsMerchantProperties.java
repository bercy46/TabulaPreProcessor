package ca.jpti.SuiviBudget.Configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix="desjardins-merchant")
public class DesjardinsMerchantProperties {
    private Map<String, String> matchRegex;
    private Map<String, String> categories;

    public Map<String, String> getMatchRegex() {
        return matchRegex;
    }

    public void setMatchRegex(Map<String, String> matchRegex) {
        this.matchRegex = matchRegex;
    }

    public Map<String, String> getCategories() {
        return categories;
    }

    public void setCategories(Map<String, String> categories) {
        this.categories = categories;
    }
}
