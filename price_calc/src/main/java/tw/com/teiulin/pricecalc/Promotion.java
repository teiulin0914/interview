package tw.com.teiulin.pricecalc;

import java.math.BigDecimal;

public abstract class Promotion implements Comparable<Promotion> {

    private int priority;
    private String uniqueFlag;

    public Promotion (int priority, String uniqueFlag) {
        this.priority = priority;
        this.uniqueFlag = uniqueFlag;
    }

    public abstract BigDecimal execute(Order order);

    public int getPriority() {
        return priority;
    }

    public String getUniqueFlag() {
        return uniqueFlag;
    }

    @Override
    public int compareTo(Promotion o) {
        return this.priority - o.getPriority();
    }
}
