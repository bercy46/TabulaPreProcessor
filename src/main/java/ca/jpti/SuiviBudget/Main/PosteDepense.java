package ca.jpti.SuiviBudget.Main;

import ca.jpti.SuiviBudget.Configuration.MerchantProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
public class PosteDepense {
    MerchantProperties merchantProperties;
    private Scanner scanner = new Scanner(System.in);

    public PosteDepense(MerchantProperties merchantProperties) {
        this.merchantProperties = merchantProperties;
    }

    public String getPosteDepense(String originalDesc, Transaction transaction, Set<String> unmatchedLabels) {
        String posteDepense = null;
//        if ("IGNORER".equals(transaction.getCategorie())) {
//            return null;
//        }
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

        if (posteDepense == null) {
            String userInput = null;
            Set<String> setCategories = new HashSet<>(merchantProperties.getCategories().values());
            List<String> categories = new ArrayList<>(setCategories);
            Collections.sort(categories);
            Map<Integer, String> mapCategories = new HashMap<>();
            StringBuffer tableau = new StringBuffer();
            for (int i = 0; i < categories.size(); i++) {
                mapCategories.put(i + 1, categories.get(i));
                tableau.append(i + 1).append(". ").append(categories.get(i)).append("\n");
            }
            tableau.append(categories.size() + 1).append(". ").append("IGNORER").append("\n");
            System.out.print(tableau + "SVP choisir un poste de depense pour "
                    + transaction.getInstitution()
                    + " "
                    + transaction.getCompte()
                    + " "
                    + transaction.getDate()
                    + " "
                    + originalDesc
                    + " "
                    + (transaction.getDebit().compareTo(BigDecimal.ZERO) == 0 ?
                    (transaction.getCredit() + " (credit)") :
                    (transaction.getDebit() + " (debit)"))
                    + ": ");

            while (userInput == null || !(userInput.matches("^[\\d]+$")) || Integer.parseInt(userInput) < 1 || Integer.parseInt(userInput) > categories.size() + 1) {
                userInput = scanner.next();
                if (userInput.matches("^[\\d]+$")) {
                    int id = Integer.parseInt(userInput);
                    if (id >= 1 && id <= categories.size() + 1) {
                        if (id == categories.size() + 1) {
                            posteDepense = "IGNORER";
                        } else {
                            posteDepense = categories.get(id - 1);
                        }
                    }
                }
            }

        }
        return posteDepense;
    }
}
