package tw.com.teiulin.pricecalc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * （加分題）訂單滿 X 元折 Y 元，此折扣在全站每個月折扣上限為 N 元
 */
public class Promotion6 extends Promotion {

    private final Map<String, BigDecimal> discountRecordPerMonth;

    private Lock lock = new ReentrantLock();

    private final BigDecimal amount;
    private final BigDecimal discountAmount;
    private final BigDecimal discountLimit;

    public Promotion6(BigDecimal amount, BigDecimal discountAmount, BigDecimal discountLimit) {
        super(100, null);
        this.amount = amount;
        this.discountAmount = discountAmount;
        this.discountLimit = discountLimit;
        this.discountRecordPerMonth = new ConcurrentHashMap<>();
    }

    public Promotion6(int priority, String uniqueFlag,
                      BigDecimal amount, BigDecimal discountAmount, BigDecimal discountLimit) {
        super(priority, uniqueFlag);
        this.amount = amount;
        this.discountAmount = discountAmount;
        this.discountLimit = discountLimit;
        this.discountRecordPerMonth = new ConcurrentHashMap<>();
    }

    @Override
    public BigDecimal execute(Order order) {
        lock.lock();
        try {
            if(order.getTotalPrice().compareTo(amount) >= 0) {
                String yyyyMM = order.getTime().substring(0, 6);

                BigDecimal record = discountRecordPerMonth.get(yyyyMM);

                if(record == null) {
                    record = discountLimit;
                }

                BigDecimal rest = record.subtract(discountAmount);

                if(rest.compareTo(BigDecimal.ZERO) < 0) {
                    discountRecordPerMonth.put(yyyyMM, BigDecimal.ZERO);
                    return record;

                } else {
                    discountRecordPerMonth.put(yyyyMM, rest);
                    return discountAmount;
                }
            }

        } finally {
            lock.unlock();
        }

        return BigDecimal.ZERO;
    }
}
