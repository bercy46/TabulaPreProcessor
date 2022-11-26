package ca.jpti.SuiviBudget.Main;

public enum ReportTypeEnum {
    DETAILED("Detailed"),
    SUMMARY("Summary"),
    EXPENSES("PostesDepenses");

    public final String label;

    private ReportTypeEnum(String label) {
        this.label = label;
    }
}
