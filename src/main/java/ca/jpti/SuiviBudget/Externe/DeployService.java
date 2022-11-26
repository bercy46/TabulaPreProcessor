package ca.jpti.SuiviBudget.Externe;

import jcifs.CIFSContext;
import jcifs.context.SingletonContext;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

@Component
@Slf4j
public class DeployService {
    @Value("${files.weeklyPostesDepensesReport}")
    private String weeklyPostesDepensesReport;
    @Value("${files.weeklyDetailedReport}")
    private String weeklyDetailedReport;
    @Value("${files.weeklySummaryReport}")
    private String weeklySummaryReport;
    @Value("${files.biweeklyPostesDepensesReport}")
    private String biweeklyPostesDepensesReport;
    @Value("${files.biweeklyDetailedReport}")
    private String biweeklyDetailedReport;
    @Value("${files.biweeklySummaryReport}")
    private String biweeklySummaryReport;
    @Value("${files.monthlyPostesDepensesReport}")
    private String monthlyPostesDepensesReport;
    @Value("${files.monthlyDetailedReport}")
    private String monthlyDetailedReport;
    @Value("${files.monthlySummaryReport}")
    private String monthlySummaryReport;

    @Autowired
    private MailService mailService;

    private Scanner scanner = new Scanner(System.in);

    public void deploy() {

        CIFSContext base = SingletonContext.getInstance();
        CIFSContext authed1 = base.withCredentials(new NtlmPasswordAuthentication(base, null,
                System.getenv("windowsuser"), System.getenv("windowspassword")));

        List<String> files = List.of(weeklyPostesDepensesReport,
                weeklyDetailedReport,
                weeklySummaryReport,
                biweeklyPostesDepensesReport,
                biweeklyDetailedReport,
                biweeklySummaryReport,
                monthlyPostesDepensesReport,
                monthlyDetailedReport,
                monthlySummaryReport);
        for (String file : files) {
            String htmlFile = file.replace(".txt", ".html");
            try {
                Path path = Paths.get(htmlFile);
                String htmlContents = Files.readString(path);
                SmbFile f = new SmbFile("smb://172.24.101.14/suivibudget/" + htmlFile, authed1);
                SmbFileOutputStream sfos = new SmbFileOutputStream(f);
                sfos.write(htmlContents.getBytes());
                sfos.close();

                if ("weeklySummaryReport.html".equals(htmlFile)) {
                    f = new SmbFile("smb://172.24.101.14/suivibudget/index.html", authed1);
                    sfos = new SmbFileOutputStream(f);
                    sfos.write(htmlContents.getBytes());
                    sfos.close();
                }
                log.info("File {} deployed", htmlFile);

            } catch (IOException e) {
                log.error("Exception deploying " + htmlFile, e);
                return;
            }
        }
        System.out.print("Envoyer le courriel ? (y/n)");
        String userInput = scanner.next();
        if (userInput.matches("^[yYoO]$")) {
            mailService.sendMessage();
        }

    }
}
