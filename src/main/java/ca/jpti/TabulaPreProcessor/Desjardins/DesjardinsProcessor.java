package ca.jpti.TabulaPreProcessor.Desjardins;

import ca.jpti.TabulaPreProcessor.Configuration.DesjardinsMerchantProperties;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

@Component
public class DesjardinsProcessor {
    private DesjardinsMerchantProperties desjardinsMerchantProperties;

    @Value("${file.input.desjardins}")
    private String fileInput;

    private int lastMonth;
    private int lastDay;
    private final List<String> accounts = Arrays.asList("Nadine", "Jacques", "Juliette", "Gabrielle");
    private int accountIdx = 0;
    private Set<String> unmatchedLabels = new HashSet<>();

    public DesjardinsProcessor(DesjardinsMerchantProperties desjardinsMerchantProperties) {
        this.desjardinsMerchantProperties = desjardinsMerchantProperties;
    }
    public void process() {
        Resource resource = new ClassPathResource(fileInput);
        File file = null;
        try {
            file = resource.getFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        StringBuffer sb = new StringBuffer();
        while (true) {
            try {
                String line = bufferedReader.readLine();
                if (line == null) break;
                String output = processLine(line);
                if (output != null)
                    sb.append(output).append("\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.print(sb.toString());
        System.out.println("Unmatched labels: " + unmatchedLabels);
    }

    private String processLine(String line) {
        if (line.startsWith("\""))
            return null;
        line = line.replaceAll("\"(\\d+)\\s+(\\d+)", "\"$1$2");
        line = line.replaceAll("\"(\\d),(\\d\\d)\\s(%)\"", "$1\\.$2$3");
        line = line.replaceAll("\"(\\d+),(\\d+)\"", "$1\\.$2");
        line = line.replaceAll("\"(\\d+),(\\d+)CR\"", "-$1\\.$2");
        String[] tokens = line.split(",");
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].matches(".*[a-zA-Z].*")
                && tokens[i+1].matches(".*[a-zA-Z].*")) {
                tokens[i] = tokens[i]+tokens[i+1];
                tokens = ArrayUtils.remove(tokens, i+1);
                break;
            }
        }
        Set<String> matchKeys = desjardinsMerchantProperties.getMatchRegex().keySet();
        Map<String, String> map = desjardinsMerchantProperties.getMatchRegex();
        for (String key : desjardinsMerchantProperties.getMatchRegex().keySet()) {
            String regex = ".*" + key + ".*";
            if (tokens[4].matches(regex)) {
                tokens[4] = map.get(key);
            }
        }
        String category = desjardinsMerchantProperties.getCategories().get(tokens[4]);
        if (category == null) {
            unmatchedLabels.add(tokens[4]+"\n");
            category = "";
        }

        int month = Integer.parseInt(tokens[1]);
        int day = Integer.parseInt(tokens[0]);
        String account = accounts.get(accountIdx);
        if (month < lastMonth || month == lastMonth && day < lastDay || month == 12 && lastMonth == 1) {
            account = accounts.get(++accountIdx);
        }

        line = tokens[0]+","+
                tokens[1]+","+
                tokens[2]+","+
                tokens[3]+","+
                account+","+
                tokens[4]+","+
                tokens[tokens.length-2]+","+
                tokens[tokens.length-1]+","+
                category;
//        tokens = line.split(",");
//        System.out.println(tokens.length + " " + line);

        lastMonth = Integer.parseInt(tokens[1]);
        lastDay = Integer.parseInt(tokens[0]);
        return line;
    }
}
