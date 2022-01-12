package tw.com.teiulin.pricecalc;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class Promotion1Test {

    /**
     * 訂單滿 X 元折 Z %
     */
    @Test
    public void test() {
        // 訂單滿100元折10%
        Calculator calculator =
                new Calculator(List.of(new Promotion1(new BigDecimal("100"), 10)));

        User user = new User(UUID.randomUUID().toString());

        // case 1: 未達折扣標準
        Order order = user.prepareOrder("20220101 12:30:01");
        order.addProduct(new Product("TX-001", "無糖綠茶", new BigDecimal("25")));
        order.addProduct(new Product("TX-002", "洋芋片", new BigDecimal("50")));
        order.addProduct(new Product("TX-003", "飯糰", new BigDecimal("24")));

        calculator.execute(order);

        Assert.assertEquals(3, order.getProducts().size());
        Assert.assertEquals(0, new BigDecimal("99").compareTo(order.getTotalPrice()));
        Assert.assertEquals(0, new BigDecimal("0").compareTo(order.getDiscountPrice()));
        Assert.assertEquals(0, new BigDecimal("99").compareTo(order.getCheckoutPrice()));


        // case 2: 剛好達折扣標準
        Order order2 = user.prepareOrder("20220111 12:30:02");
        order2.addProduct(new Product("TX-001", "無糖綠茶", new BigDecimal("25")));
        order2.addProduct(new Product("TX-002", "洋芋片", new BigDecimal("50")));
        order2.addProduct(new Product("TX-003", "飯糰", new BigDecimal("25")));

        calculator.execute(order2);

        Assert.assertEquals(3, order2.getProducts().size());
        Assert.assertEquals(0, new BigDecimal("100").compareTo(order2.getTotalPrice()));
        Assert.assertEquals(0, new BigDecimal("10").compareTo(order2.getDiscountPrice()));
        Assert.assertEquals(0, new BigDecimal("90").compareTo(order2.getCheckoutPrice()));


        // case 3: 超過折扣標準
        Order order3 = user.prepareOrder("20220111 12:30:03");
        order3.addProduct(new Product("TX-001", "無糖綠茶", new BigDecimal("25")));
        order3.addProduct(new Product("TX-002", "洋芋片", new BigDecimal("50")));
        order3.addProduct(new Product("TX-003", "飯糰", new BigDecimal("26")));

        calculator.execute(order3);

        Assert.assertEquals(3, order3.getProducts().size());
        Assert.assertEquals(0, new BigDecimal("101").compareTo(order3.getTotalPrice()));
        Assert.assertEquals(0, new BigDecimal("10.1").compareTo(order3.getDiscountPrice()));
        Assert.assertEquals(0, new BigDecimal("90.9").compareTo(order3.getCheckoutPrice()));
    }

}
