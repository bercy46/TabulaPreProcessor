package ca.jpti.SuiviBudget.Main;

public enum PeriodEnum {
    WEEKLY("Hebdomadaire"),
    BIWEEKLY("Bi-Hebdo"),
    MONTHLY("Mensuel");

    public final String label;

    private PeriodEnum(String label) {
        this.label = label;
    }
}
