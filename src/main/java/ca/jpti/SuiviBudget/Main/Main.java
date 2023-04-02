package ca.jpti.SuiviBudget.Main;

import ca.jpti.SuiviBudget.Desjardins.DesjardinsJsonProcessor;
import ca.jpti.SuiviBudget.Externe.DeployService;
import ca.jpti.SuiviBudget.Externe.DesjardinsClient;
import ca.jpti.SuiviBudget.TD.TDProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;

@Component
@Slf4j
public class Main {
    private TDProcessor tdProcessor;
    private DesjardinsJsonProcessor desjardinsJsonProcessor;
    private DesjardinsClient desjardinsClient;
    private DeployService deployService;

    @Value("${startDate}")
    private String startDate;
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
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public Main(TDProcessor tdProcessor, DesjardinsJsonProcessor desjardinsJsonProcessor, DesjardinsClient desjardinsClient, DeployService deployService) {
        this.tdProcessor = tdProcessor;
        this.desjardinsJsonProcessor = desjardinsJsonProcessor;
        this.desjardinsClient = desjardinsClient;
        this.deployService = deployService;
    }

    @PostConstruct
    public void process() throws IOException {
        TransactionReport tdReport = tdProcessor.process();
        TransactionReport desjardinsInfiniteReport = desjardinsJsonProcessor.process("VISA Infinite");
        TransactionReport desjardinsWorldReport = desjardinsJsonProcessor.process("MC World");
        doReports(tdReport, desjardinsInfiniteReport, desjardinsWorldReport);
        deployService.deploy();
    }

    private void doReports(TransactionReport tdReport, TransactionReport desjardinsInfiniteReport, TransactionReport desjardinsWorldReport) {
        float totalAutorisees = 0;
        log.info("Transactions TD: " + tdReport);
        log.info("Transactions Desjardins Infinite: " + desjardinsInfiniteReport);
        totalAutorisees += desjardinsInfiniteReport.getTotalAutorisees();
        log.info("Transactions Desjardins World: " + desjardinsWorldReport);
        totalAutorisees += desjardinsWorldReport.getTotalAutorisees();

        List<Transaction> transactions = new ArrayList<>();
        transactions.addAll(tdReport.getTransactions());
        transactions.addAll(desjardinsInfiniteReport.getTransactions());
        transactions.addAll(desjardinsWorldReport.getTransactions());

        creerRapportSommaire(transactions, totalAutorisees, PeriodEnum.WEEKLY, weeklySummaryReport);
        creerRapportSommaireHTML(transactions, totalAutorisees, PeriodEnum.WEEKLY, weeklySummaryReport);
        creerRapportDetaille(transactions, totalAutorisees, PeriodEnum.WEEKLY, weeklyDetailedReport);
        creerRapportDetailleHTML(transactions, totalAutorisees, PeriodEnum.WEEKLY, weeklyDetailedReport);
        creerRapportPostesDepenses(transactions, totalAutorisees, PeriodEnum.WEEKLY, weeklyPostesDepensesReport);
        creerRapportPostesDepensesHTML(transactions, totalAutorisees, PeriodEnum.WEEKLY, weeklyPostesDepensesReport);

        creerRapportSommaire(transactions, totalAutorisees, PeriodEnum.BIWEEKLY, biweeklySummaryReport);
        creerRapportSommaireHTML(transactions, totalAutorisees, PeriodEnum.BIWEEKLY, biweeklySummaryReport);
        creerRapportDetaille(transactions, totalAutorisees, PeriodEnum.BIWEEKLY, biweeklyDetailedReport);
        creerRapportDetailleHTML(transactions, totalAutorisees, PeriodEnum.BIWEEKLY, biweeklyDetailedReport);
        creerRapportPostesDepenses(transactions, totalAutorisees, PeriodEnum.BIWEEKLY, biweeklyPostesDepensesReport);
        creerRapportPostesDepensesHTML(transactions, totalAutorisees, PeriodEnum.BIWEEKLY, biweeklyPostesDepensesReport);

        creerRapportSommaire(transactions, totalAutorisees, PeriodEnum.MONTHLY, monthlySummaryReport);
        creerRapportSommaireHTML(transactions, totalAutorisees, PeriodEnum.MONTHLY, monthlySummaryReport);
        creerRapportDetaille(transactions, totalAutorisees, PeriodEnum.MONTHLY, monthlyDetailedReport);
        creerRapportDetailleHTML(transactions, totalAutorisees, PeriodEnum.MONTHLY, monthlyDetailedReport);
        creerRapportPostesDepenses(transactions, totalAutorisees, PeriodEnum.MONTHLY, monthlyPostesDepensesReport);
        creerRapportPostesDepensesHTML(transactions, totalAutorisees, PeriodEnum.MONTHLY, monthlyPostesDepensesReport);
    }

    private void creerRapportPostesDepenses(List<Transaction> transactions, float totalAutorisees, PeriodEnum periodEnum, String reportPath) {
        Set<PostesDepensesReport> postesDepensesReports = new HashSet<>();
        if (periodEnum == PeriodEnum.WEEKLY) {
            postesDepensesReports = createPostesDepensesReports(transactions, PeriodEnum.WEEKLY);
        } else if (periodEnum == PeriodEnum.BIWEEKLY) {
            postesDepensesReports = createPostesDepensesReports(transactions, PeriodEnum.BIWEEKLY);
        } else {
            postesDepensesReports = createPostesDepensesReports(transactions, PeriodEnum.MONTHLY);
        }
        List<PostesDepensesReport> listPostesDepensesReports = new ArrayList<>(postesDepensesReports);
        Collections.reverse(listPostesDepensesReports);
        TotalPosteDepenseComparator comparator = new TotalPosteDepenseComparator();
        StringBuffer sb = new StringBuffer();
        sb.append("Date: ").append(formatter.format(LocalDateTime.now())).append("\n");
        ;
        sb.append("---------------------------------------------------------\n")
                .append("Période                 Poste de Dépenses         Montant\n")
                .append("---------------------------------------------------------\n");
        String currentPeriod = null;
        List<PostesDepensesReport> listePostesDepensesReport = new ArrayList<>();
        boolean autoriseesDone = false;
        for (PostesDepensesReport postesDepensesReport : listPostesDepensesReports) {
            Collections.sort(postesDepensesReport.getTotauxPostesDepenses(), comparator);
            for (TotalPosteDepense totalPosteDepense : postesDepensesReport.getTotauxPostesDepenses()) {
                sb.append(String.format("%-24s", postesDepensesReport.getPeriod().equals(currentPeriod) ? "" : postesDepensesReport.getPeriod()))
                        .append(String.format("%-26s", totalPosteDepense.getPosteDepense()))
                        .append(String.format("%-9.02f", totalPosteDepense.getMontant()))
                        .append("\n");
                currentPeriod = postesDepensesReport.getPeriod();
            }
            sb.append("Total: ")
                    .append(postesDepensesReport.getTotal());
            if (!autoriseesDone) {
                sb.append(" (Autorisées: ")
                        .append(String.format("%.02f", totalAutorisees))
                        .append(")");
                autoriseesDone = true;
            }
            sb.append("\n\n");
        }
        Path path = Paths.get(reportPath);
        System.out.println("Output file: " + path.toAbsolutePath());
        try {
            Files.write(path, sb.toString().getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void creerRapportPostesDepensesHTML(List<Transaction> transactions, float totalAutorisees, PeriodEnum periodEnum, String reportPath) {
        Set<PostesDepensesReport> postesDepensesReports = new HashSet<>();
        if (periodEnum == PeriodEnum.WEEKLY) {
            postesDepensesReports = createPostesDepensesReports(transactions, PeriodEnum.WEEKLY);
        } else if (periodEnum == PeriodEnum.BIWEEKLY) {
            postesDepensesReports = createPostesDepensesReports(transactions, PeriodEnum.BIWEEKLY);
        } else {
            postesDepensesReports = createPostesDepensesReports(transactions, PeriodEnum.MONTHLY);
        }
        List<PostesDepensesReport> listPostesDepensesReports = new ArrayList<>(postesDepensesReports);
        Collections.reverse(listPostesDepensesReports);
        TotalPosteDepenseComparator comparator = new TotalPosteDepenseComparator();
        StringBuffer sb = new StringBuffer();
        sb.append("<HTML>\n");
        sb.append("<HEAD>\n");
        sb.append("<meta charset=\"UTF-8\">\n" +
                "<meta http-equiv=\"Content-type\" content=\"text/html; charset=UTF-8\">\n");
        sb.append("<style>\ntable, th, td {\nborder: 0px solid black;\n}\n</style>");
        sb.append("</HEAD>\n");
        sb.append("<BODY>\n");
        sb.append(headerHTML(ReportTypeEnum.EXPENSES, periodEnum));
        sb.append("<H3>Date: ").append(formatter.format(LocalDateTime.now())).append(" - ").append("Postes de dépense").append(" - ").append(periodEnum.label).append("</H3><p>\n");
        String currentPeriod = null;
        List<PostesDepensesReport> listePostesDepensesReport = new ArrayList<>();
        boolean autoriseesDone = false;
        for (PostesDepensesReport postesDepensesReport : listPostesDepensesReports) {
            sb.append("<TABLE>\n");
            sb.append("<TR><TH align=left>Période</TH>\n");
            sb.append("<TH align=left>Poste de Dépenses</TH>\n");
            sb.append("<TH align=left>Montant</TH><TH></TH></TR>\n");
            Collections.sort(postesDepensesReport.getTotauxPostesDepenses(), comparator);
            for (TotalPosteDepense totalPosteDepense : postesDepensesReport.getTotauxPostesDepenses()) {
                sb.append("<TR>\n");
                sb.append("<TD>").append(postesDepensesReport.getPeriod().equals(currentPeriod) ? "" : postesDepensesReport.getPeriod()).append("</TD>\n");
                sb.append("<TD>").append(totalPosteDepense.getPosteDepense()).append("</TD>\n");
                sb.append("<TD align=right>").append(String.format("%.02f", totalPosteDepense.getMontant())).append("</TD>\n");
                sb.append("<TD></TD></TR>\n");
                currentPeriod = postesDepensesReport.getPeriod();
            }
            sb.append("<TR><TD COLSPAN=2 align=left>Total: </TD><TD align=right>")
                    .append(postesDepensesReport.getTotal())
                    .append("</TD>\n");
            if (!autoriseesDone) {
                sb.append("<TD>(Autorisées: ")
                        .append(String.format("%.02f", totalAutorisees))
                        .append(")</TD>\n");
                autoriseesDone = true;
            } else {
                sb.append("<TD></TD>\n");
            }
            sb.append("</TR></TABLE><p>\n");
        }
        sb.append("</BODY></HTML>\n");

        Path path = Paths.get(reportPath.replace(".txt", ".html"));
        System.out.println("Output file: " + path.toAbsolutePath());
        try {
            Files.write(path, sb.toString().getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void creerRapportDetaille(List<Transaction> transactions, float totalAutorisees, PeriodEnum periodEnum, String reportPath) {
        Set<DetailedReport> detailedReports = createReports(transactions, periodEnum);
        List<DetailedReport> listDetailedReports = new ArrayList<>(detailedReports);
        Collections.reverse(listDetailedReports);
        StringBuffer sb = new StringBuffer();
        sb.append("Date: ").append(formatter.format(LocalDateTime.now())).append("\n");
        ;
        boolean autoriseesDone = false;
        for (DetailedReport report : listDetailedReports) {
            sb.append("\nPériode: ")
                    .append(report.getPeriod())
                    .append(" - Dépenses fixes: ")
                    .append(report.getTransactionReport().getTotalDepensesFixes())
                    .append(" - Dépenses variables: ")
                    .append(report.getTransactionReport().getTotalDepensesVariables());
            if (!autoriseesDone) {
                sb.append(" (Autorisées: ")
                        .append(String.format("%.02f", totalAutorisees))
                        .append(")");
                autoriseesDone = true;
            }
            sb.append("\n")
                    .append(tableauDepenses(report.getTransactionReport().getTransactions()));
        }
        Path path = Paths.get(reportPath);
        System.out.println("Output file: " + path.toAbsolutePath());
        try {
            Files.write(path, sb.toString().getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void creerRapportDetailleHTML(List<Transaction> transactions, float totalAutorisees, PeriodEnum periodEnum, String reportPath) {
        Set<DetailedReport> detailedReports = createReports(transactions, periodEnum);
        List<DetailedReport> listDetailedReports = new ArrayList<>(detailedReports);
        Collections.reverse(listDetailedReports);
        StringBuffer sb = new StringBuffer();
        sb.append("<HTML>\n");
        sb.append("<HEAD>\n");
        sb.append("<meta charset=\"UTF-8\">\n" +
                "<meta http-equiv=\"Content-type\" content=\"text/html; charset=UTF-8\">\n");
        sb.append("<style>\ntable, th, td {\nborder: 0px solid black;\n}\n</style>");
        sb.append("</HEAD>\n");
        sb.append("<BODY>\n");
        sb.append(headerHTML(ReportTypeEnum.DETAILED, periodEnum));
        sb.append("<H3>Date: ").append(formatter.format(LocalDateTime.now())).append(" - ").append("Détaillé").append(" - ").append(periodEnum.label).append("</H3><p>\n");
        boolean autoriseesDone = false;
        sb.append("<TABLE>\n");
        for (DetailedReport report : listDetailedReports) {
            sb.append("<TR><TH colspan=7 align=left>\n");
            sb.append("Période: ")
                    .append(report.getPeriod())
                    .append(" - Dépenses fixes: ")
                    .append(report.getTransactionReport().getTotalDepensesFixes())
                    .append(" - Dépenses variables: ")
                    .append(report.getTransactionReport().getTotalDepensesVariables());
            if (!autoriseesDone) {
                sb.append(" (Autorisées: ")
                        .append(String.format("%.02f", totalAutorisees))
                        .append(")");
                autoriseesDone = true;
            }
            sb.append("</TH></TR>\n")
                    .append(tableauDepensesHTML(report.getTransactionReport().getTransactions()));
            sb.append("<TR><TD COLSPAN=7></TD></TR>\n");
            sb.append("<TR><TD COLSPAN=7></TD></TR>\n");
            sb.append("<TR><TD COLSPAN=7></TD></TR>\n");
        }
        sb.append("</BODY></HTML>\n");

        Path path = Paths.get(reportPath.replace(".txt", ".html"));
        System.out.println("Output file: " + path.toAbsolutePath());
        try {
            Files.write(path, Collections.singleton(sb), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void creerRapportSommaireHTML(List<Transaction> transactions, float totalAutorisees, PeriodEnum periodEnum, String reportPath) {
        Set<DetailedReport> detailedReports = createReports(transactions, periodEnum);
        List<DetailedReport> listDetailedReports = new ArrayList<>(detailedReports);
        Collections.reverse(listDetailedReports);
        StringBuffer sb = new StringBuffer();
        sb.append("<HTML>\n");
        sb.append("<HEAD>\n");
        sb.append("<meta charset=\"UTF-8\">\n" +
                "<meta http-equiv=\"Content-type\" content=\"text/html; charset=UTF-8\">\n");
        sb.append("<style>\ntable, th, td {\nborder: 0px solid black;\n}\n</style>");
        sb.append("</HEAD>\n");
        sb.append("<BODY>\n");
        sb.append(headerHTML(ReportTypeEnum.SUMMARY, periodEnum));
        sb.append("<H3>Date: ").append(formatter.format(LocalDateTime.now())).append(" - ").append("Sommaire").append(" - ").append(periodEnum.label).append("</H3><p>\n");
        sb.append("<TABLE>\n<TR>\n");
        sb.append("<TH align=left>Période</TH><TH>Dépenses fixes</TH><TH>Dépenses variables</TH></TR>\n");
        sb.append("<TR><TD COLSPAN=2>* Autorisées *</TD>\n");
        sb.append("<TD align=right>").append(String.format("%.02f", totalAutorisees)).append("</TD></TR>\n");
        for (DetailedReport report : listDetailedReports) {
            sb.append("<TR>\n");
            sb.append("<TD>").append(report.getPeriod()).append("</TD>\n");
            sb.append("<TD align=right>").append(String.format("%.02f", report.getTransactionReport().getTotalDepensesFixes())).append("</TD>\n");
            sb.append("<TD align=right>").append(String.format("%.02f", report.getTransactionReport().getTotalDepensesVariables())).append("</TD>\n");
            sb.append("</TR>\n");
        }
        sb.append("</TABLE>\n</BODY>\n</HTML>\n");

        Path path = Paths.get(reportPath.replace(".txt", ".html"));
        System.out.println("Output file: " + path.toAbsolutePath());
        try {
            Files.write(path, Collections.singleton(sb), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void creerRapportSommaire(List<Transaction> transactions, float totalAutorisees, PeriodEnum periodEnum, String reportPath) {
        Set<DetailedReport> detailedReports = createReports(transactions, periodEnum);
        List<DetailedReport> listDetailedReports = new ArrayList<>(detailedReports);
        Collections.reverse(listDetailedReports);
        StringBuffer sb = new StringBuffer();
        sb.append("Date: ").append(formatter.format(LocalDateTime.now())).append("\n");
        sb.append("-----------------------------------------------------------\n")
                .append("Période                 Dépenses fixes   Dépenses variables\n")
                .append("-----------------------------------------------------------\n");
        sb.append(String.format("%-24s", "* Autorisées *"))
                .append(String.format("%-17s", ""))
                .append(String.format("%-17.02f", totalAutorisees))
                .append("\n");
        for (DetailedReport report : listDetailedReports) {
            sb.append(String.format("%-24s", report.getPeriod()))
                    .append(String.format("%-17.02f", report.getTransactionReport().getTotalDepensesFixes()))
                    .append(String.format("%-17.02f", report.getTransactionReport().getTotalDepensesVariables()))
                    .append("\n");
        }
        Path path = Paths.get(reportPath);
        System.out.println("Output file: " + path.toAbsolutePath());
        try {
            Files.write(path, sb.toString().getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String tableauDepenses(List<Transaction> transactions) {
        String tableau = "-----------------------------------------------------------------------------------------------------------\n";
        tableau += "Date       V/F Description                                Debit    Credit   Compte        Poste de dépenses\n";
        tableau += "-----------------------------------------------------------------------------------------------------------\n";
        String[] types = new String[]{"Fixe", "Variable"};
        for (String type : types) {
            Collections.sort(transactions);
            for (Transaction transaction : transactions) {
                if (type.equals(transaction.getCategorie())) {
                    tableau += String.format("%-11s", transaction.getDate().format(formatter));
                    tableau += type.substring(0, 1) + "   ";
                    tableau += String.format("%-43s", transaction.getDescription());
                    tableau += String.format("%-9.2f", transaction.getDebit().doubleValue());
                    tableau += String.format("%-9.2f", transaction.getCredit().doubleValue());
                    tableau += String.format("%-14s", transaction.getCompte());
                    tableau += transaction.getPosteDepense() + "\n";
                }
            }
        }
        return tableau;
    }

    private String tableauDepensesHTML(List<Transaction> transactions) {
        String tableau = "<TR>\n";
        tableau += "<TH align=left>Date</TH>\n";
        tableau += "<TH align=left>V/F</TH>\n";
        tableau += "<TH align=left>Description</TH>\n";
        tableau += "<TH align=left>Débit</TH>\n";
        tableau += "<TH align=left>Crébit</TH>\n";
        tableau += "<TH align=left>Compte</TH>\n";
        tableau += "<TH align=left>Poste de dépenses</TH>\n";
        tableau += "</TR>\n";
        String[] types = new String[]{"Fixe", "Variable"};
        for (String type : types) {
            Collections.sort(transactions);
            for (Transaction transaction : transactions) {
                if (type.equals(transaction.getCategorie())) {
                    tableau += "<TR>\n";
                    tableau += "<TD>" + transaction.getDate().format(formatter) + "</TD>\n";
                    tableau += "<TD>" + type.substring(0, 1) + "</TD>\n";
                    tableau += "<TD>" + transaction.getDescription() + "</TD>\n";
                    tableau += "<TD align=right>" + String.format("%.02f", transaction.getDebit().doubleValue()) + "</TD>\n";
                    tableau += "<TD align=right>" + String.format("%.02f", transaction.getCredit().doubleValue()) + "</TD>\n";
                    tableau += "<TD>" + transaction.getCompte() + "</TD>\n";
                    tableau += "<TD>" + transaction.getPosteDepense() + "</TD>\n";
                    tableau += "</TR>\n";
                }
            }
        }
        return tableau;
    }


    private String getPeriod(LocalDate transactionDate, PeriodEnum periodEnum) {
        if (periodEnum == PeriodEnum.WEEKLY) {
            return getWeeklyPeriod(transactionDate);
        } else if (periodEnum == PeriodEnum.BIWEEKLY) {
            return getBiWeeklyPeriod(transactionDate);
        }
        return getMonthlyPeriod(transactionDate);
    }

    private Set<PostesDepensesReport> createPostesDepensesReports(List<Transaction> transactions, PeriodEnum periodEnum) {
        Set<PostesDepensesReport> reports = new TreeSet<>();
        for (Transaction transaction : transactions) {
            String posteDepense = transaction.getPosteDepense();
            if (StringUtils.equals(transaction.getCategorie(), "IGNORER")
                    || StringUtils.equals(transaction.getCategorie(), "Fixe")
                    || posteDepense == null
                    || StringUtils.equals(posteDepense, "IGNORER")
            ) {
                continue;
            }
            String period = getPeriod(transaction.getDate(), periodEnum);
            PostesDepensesReport report = null;
            for (PostesDepensesReport postesDepensesReport : reports) {
                if (postesDepensesReport.getPeriod().equals(period)) {
                    report = postesDepensesReport;
                    break;
                }
            }
            if (report == null) {
                report = new PostesDepensesReport();
                report.setPeriod(period);
                reports.add(report);
            }

            BigDecimal montant = transaction.getDebit().subtract(transaction.getCredit());
            TotalPosteDepense totalPosteDepenseAjoute = null;
            for (TotalPosteDepense totalPosteDepense : report.getTotauxPostesDepenses()) {
                if (totalPosteDepense.getPosteDepense().equals(posteDepense)) {
                    totalPosteDepense.setMontant(totalPosteDepense.getMontant().add(montant));
                    totalPosteDepenseAjoute = totalPosteDepense;
                    break;
                }
            }

            if (totalPosteDepenseAjoute == null) {
                TotalPosteDepense totalPosteDepense = new TotalPosteDepense();
                totalPosteDepense.setPosteDepense(posteDepense);
                totalPosteDepense.setMontant(montant);
                report.getTotauxPostesDepenses().add(totalPosteDepense);
                totalPosteDepenseAjoute = totalPosteDepense;
            }
        }

        for (PostesDepensesReport report : reports) {
            for (TotalPosteDepense totalPosteDepense : report.getTotauxPostesDepenses()) {
                report.setTotal(report.getTotal().add(totalPosteDepense.getMontant()));
            }
        }

        return reports;
    }

    private Set<DetailedReport> createReports(List<Transaction> transactions, PeriodEnum periodEnum) {
        Set<DetailedReport> detailedReports = new TreeSet<>();
        for (Transaction transaction : transactions) {
            String period = null;
            if (periodEnum == PeriodEnum.WEEKLY) {
                period = getWeeklyPeriod(transaction.getDate());
            } else if (periodEnum == PeriodEnum.BIWEEKLY) {
                period = getBiWeeklyPeriod(transaction.getDate());
            } else {
                period = getMonthlyPeriod(transaction.getDate());
            }
            DetailedReport report = null;
            for (DetailedReport detailedReport : detailedReports) {
                if (detailedReport.getPeriod().equals(period)) {
                    report = detailedReport;
                    detailedReport.getTransactionReport().getTransactions().add(transaction);
                    break;
                }
            }
            if (report == null) {
                report = new DetailedReport();
                report.setPeriod(period);
                TransactionReport transactionReport = new TransactionReport();
                List<Transaction> transactionsForReport = new ArrayList<>();
                transactionsForReport.add(transaction);
                transactionReport.setTransactions(transactionsForReport);
                report.setTransactionReport(transactionReport);
                detailedReports.add(report);
            }
        }

        for (DetailedReport detailedReport : detailedReports) {
            float total = (float) detailedReport
                    .getTransactionReport()
                    .getTransactions()
                    .stream()
                    .filter(o -> "Variable".equals(o.getCategorie()))
                    .filter(o -> !"IGNORER".equals(o.getCategorie()))
                    .filter(o -> !"IGNORER".equals(o.getPosteDepense()))
                    .mapToDouble(o -> o.getDebit().doubleValue() - o.getCredit().doubleValue())
                    .sum();
            detailedReport.getTransactionReport().setTotalDepensesVariables(total);
            total = (float) detailedReport
                    .getTransactionReport()
                    .getTransactions()
                    .stream()
                    .filter(o -> "Fixe".equals(o.getCategorie()))
                    .filter(o -> !"IGNORER".equals(o.getCategorie()))
                    .filter(o -> !"IGNORER".equals(o.getPosteDepense()))
                    .mapToDouble(o -> o.getDebit().doubleValue() - o.getCredit().doubleValue())
                    .sum();
            detailedReport.getTransactionReport().setTotalDepensesFixes(total);
        }
        return detailedReports;
    }

    private String getWeeklyPeriod(LocalDate date) {
        LocalDate monday = date
                .with(
                        TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)
                );
        LocalDate sunday = monday
                .with(
                        TemporalAdjusters.next(DayOfWeek.SUNDAY)
                );
        return monday.format(formatter) + " - " + sunday.format(formatter);
    }

    private String getBiWeeklyPeriod(LocalDate date) {
        LocalDate startDate = LocalDate.parse(this.startDate);
        long nbDaysDiff = DAYS.between(startDate, date);
        long nbTwoWeekPeriodDiff = nbDaysDiff / 14;
        long remainder = nbDaysDiff % 14;
        LocalDate periodStartDate = null;
        LocalDate periodEndDate = null;
        if (nbDaysDiff >= 0) {
            periodStartDate = startDate.plusDays(nbTwoWeekPeriodDiff * 14);
        } else {
            periodStartDate = startDate.minusDays(-1 * (nbTwoWeekPeriodDiff - 1) * 14);
        }
        periodEndDate = periodStartDate.plusDays(13);
        return periodStartDate.format(formatter) + " - " + periodEndDate.format(formatter);
    }

    private String headerHTML(ReportTypeEnum reportTypeEnum, PeriodEnum periodEnum) {
        StringBuffer sb = new StringBuffer();
        sb.append("<H1>Suivi Budget</H1><p>\n");
        sb.append("<TABLE>\n");
        sb.append("<TR>\n");
        sb.append("<TH COLSPAN=3 ALIGN=LEFT>").append(ReportTypeEnum.SUMMARY.label).append("</TH>\n");
        sb.append("<TH COLSPAN=3 ALIGN=LEFT>").append(ReportTypeEnum.DETAILED.label).append("</TH>\n");
        sb.append("<TH COLSPAN=3 ALIGN=LEFT>").append(ReportTypeEnum.EXPENSES.label).append("</TH>\n");
        sb.append("</TR>\n");
        sb.append("<TR>\n");
        for (ReportTypeEnum currentReportTypeEnum : List.of(ReportTypeEnum.SUMMARY, ReportTypeEnum.DETAILED, ReportTypeEnum.EXPENSES)) {
            for (PeriodEnum currentPeriodEnum : List.of(PeriodEnum.WEEKLY, PeriodEnum.BIWEEKLY, PeriodEnum.MONTHLY)) {
                sb.append("<TD>\n");
                if (currentPeriodEnum == periodEnum && currentReportTypeEnum == reportTypeEnum) {
                    sb.append(currentPeriodEnum.label).append("\n");
                } else {
                    sb.append("<a href='./")
                            .append(currentPeriodEnum.label)
                            .append(currentReportTypeEnum.label)
                            .append("Report.html")
                            .append("'>")
                            .append(currentPeriodEnum.label)
                            .append("</a>\n");
                }
                sb.append("</TD>\n");
            }
        }
        sb.append("</TR>\n");
        sb.append("</TABLE>\n");
        return sb.toString();
    }

    private String getMonthlyPeriod(LocalDate date) {
        LocalDate premier = date.withDayOfMonth(1);
        LocalDate dernier = date.withDayOfMonth(date.getMonth().length(date.isLeapYear()));
        return premier.format(formatter) + " - " + dernier.format(formatter);
    }
}
