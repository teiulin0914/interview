package tw.com.teiulin.pricecalc;

import java.math.BigDecimal;

public abstract class Promotion {

    public abstract BigDecimal execute(Order order);

}
