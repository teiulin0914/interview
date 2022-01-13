package tw.com.teiulin.pricecalc;

import java.math.BigDecimal;
import java.util.Queue;

/**
 * 訂單滿 X 元贈送特定商品
 */
public class Promotion3 extends Promotion {

    private final BigDecimal amount;
    private final Queue<Product> presents;

    public Promotion3(BigDecimal amount, Queue<Product> presents) {
        super(100, null);
        this.amount = amount;
        this.presents = presents;
    }

    public Promotion3(int priority, String uniqueFlag,
                      BigDecimal amount, Queue<Product> presents) {
        super(priority, uniqueFlag);
        this.amount = amount;
        this.presents = presents;
    }

    @Override
    public BigDecimal execute(Order order) {

        if(order.getTotalPrice().compareTo(amount) >= 0) {
            Product present = presents.poll();

            if(present != null) {
                order.getProducts().add(present);
            }
        }

        return BigDecimal.ZERO;
    }
}
