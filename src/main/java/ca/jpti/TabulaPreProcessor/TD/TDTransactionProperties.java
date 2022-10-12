package ca.jpti.TabulaPreProcessor.TD;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix="td-transaction")
public class TDTransactionProperties {
    private Map<String, String> matchRegex;

    public Map<String, String> getMatchRegex() {
        return matchRegex;
    }

    public void setMatchRegex(Map<String, String> matchRegex) {
        this.matchRegex = matchRegex;
    }

}
