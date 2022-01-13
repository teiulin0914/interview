package tw.com.teiulin.pricecalc;

import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 訂單滿 X 元折 Y 元，此折扣在全站總共只能套⽤ N 次
 */
public class Promotion4 extends Promotion {

    private Lock lock = new ReentrantLock();

    private final BigDecimal amount;
    private final BigDecimal discountAmount;
    private Long limit;

    public Promotion4(BigDecimal amount, BigDecimal discountAmount, Long limit) {
        super(100, null);
        this.amount = amount;
        this.discountAmount = discountAmount;
        this.limit = limit;
    }

    public Promotion4(int priority, String uniqueFlag,
                      BigDecimal amount, BigDecimal discountAmount, Long limit) {
        super(priority, uniqueFlag);
        this.amount = amount;
        this.discountAmount = discountAmount;
        this.limit = limit;
    }

    @Override
    public BigDecimal execute(Order order) {
        lock.lock();
        try {
            if(order.getTotalPrice().compareTo(amount) >= 0
                    && limit > 0) {
                limit--;
                return discountAmount;
            }

        } finally {
            lock.unlock();
        }

        return BigDecimal.ZERO;
    }
}
