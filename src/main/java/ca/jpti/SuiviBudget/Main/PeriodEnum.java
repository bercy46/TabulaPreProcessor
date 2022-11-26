package ca.jpti.SuiviBudget.Main;

public enum PeriodEnum {
    WEEKLY("weekly"),
    BIWEEKLY("biweekly"),
    MONTHLY("monthly");

    public final String label;

    private PeriodEnum(String label) {
        this.label = label;
    }
}
