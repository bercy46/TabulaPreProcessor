package ca.jpti.SuiviBudget.Main;

import ca.jpti.SuiviBudget.Desjardins.DesjardinsProcessor;
import ca.jpti.SuiviBudget.TD.TDProcessor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Main {
    private TDProcessor tdProcessor;
    private DesjardinsProcessor desjardinsProcessor;

    public Main(TDProcessor tdProcessor, DesjardinsProcessor desjardinsProcessor) {
        this.tdProcessor = tdProcessor;
        this.desjardinsProcessor = desjardinsProcessor;
    }

    @PostConstruct
    public void process() {
        tdProcessor.process();
        desjardinsProcessor.process();
    }
}
