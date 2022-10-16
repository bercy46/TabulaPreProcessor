package ca.jpti.SuiviBudget.Main;

import ca.jpti.SuiviBudget.Configuration.MerchantProperties;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class PosteDepense {
    MerchantProperties merchantProperties;

    public PosteDepense(MerchantProperties merchantProperties) {
        this.merchantProperties = merchantProperties;
    }
    public String getPosteDepense(String originalDesc, Set<String> unmatchedLabels) {
        String posteDepense = null;
        Map<String, String> map = merchantProperties.getMatchRegex();
        String desc = null;
        for (String key : merchantProperties.getMatchRegex().keySet()) {
            String regex = ".*" + key + ".*";
            if (originalDesc.matches(regex)) {
                desc = map.get(key);
                break;
            }
        }
        if (desc == null) {
            desc = originalDesc;
        }

        for (String matchString : merchantProperties.getCategories().keySet()) {
            if (desc.contains(matchString)) {
                posteDepense = merchantProperties.getCategories().get(matchString);
                break;
            }
        }
        if (posteDepense == null) {
            if (originalDesc.endsWith(" IT")) {
                posteDepense = "Vacances";
            } else if (originalDesc.endsWith(" NY")) {
                posteDepense = "Vacances";
            } else {
                unmatchedLabels.add(originalDesc + "\n");
            }
        }
        return posteDepense;
    }
}
