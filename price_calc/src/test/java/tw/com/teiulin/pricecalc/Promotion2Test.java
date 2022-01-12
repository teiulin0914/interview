package tw.com.teiulin.pricecalc;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

public class Promotion2Test {

    /**
     * 特定商品滿 X 件折 Y 元
     */
    @Test
    public void test() {
        // 無糖綠茶滿2件折6元
        Calculator calculator =
                new Calculator(List.of(new Promotion2(List.of("TX-001"), Long.valueOf(2), new BigDecimal("6"))));

        User user = new User(UUID.randomUUID().toString());

        // case 1: 未達折扣標準
        Order order = user.prepareOrder("20220102 12:30:01");
        order.addProduct(new Product("TX-001", "無糖綠茶", new BigDecimal("25")));
        order.addProduct(new Product("TX-002", "洋芋片", new BigDecimal("50")));

        calculator.execute(order);

        Assert.assertEquals(2, order.getProducts().size());
        Assert.assertEquals(0, new BigDecimal("75").compareTo(order.getTotalPrice()));
        Assert.assertEquals(0, new BigDecimal("0").compareTo(order.getDiscountPrice()));
        Assert.assertEquals(0, new BigDecimal("75").compareTo(order.getCheckoutPrice()));


        // case 2: 剛好達折扣標準
        Order order2 = user.prepareOrder("20220102 12:30:02");
        order2.addProduct(new Product("TX-001", "無糖綠茶", new BigDecimal("25")));
        order2.addProduct(new Product("TX-002", "洋芋片", new BigDecimal("50")));
        order2.addProduct(new Product("TX-001", "無糖綠茶", new BigDecimal("25")));

        calculator.execute(order2);

        Assert.assertEquals(3, order2.getProducts().size());
        Assert.assertEquals(0, new BigDecimal("100").compareTo(order2.getTotalPrice()));
        Assert.assertEquals(0, new BigDecimal("6").compareTo(order2.getDiscountPrice()));
        Assert.assertEquals(0, new BigDecimal("94").compareTo(order2.getCheckoutPrice()));


        // case 2: 剛好達折扣標準
        Order order3 = user.prepareOrder("20220102 12:30:03");
        order3.addProduct(new Product("TX-001", "無糖綠茶", new BigDecimal("25")));
        order3.addProduct(new Product("TX-002", "洋芋片", new BigDecimal("50")));
        order3.addProduct(new Product("TX-001", "無糖綠茶", new BigDecimal("25")));
        order3.addProduct(new Product("TX-001", "無糖綠茶", new BigDecimal("25")));

        calculator.execute(order3);

        Assert.assertEquals(4, order3.getProducts().size());
        Assert.assertEquals(0, new BigDecimal("125").compareTo(order3.getTotalPrice()));
        Assert.assertEquals(0, new BigDecimal("6").compareTo(order3.getDiscountPrice()));
        Assert.assertEquals(0, new BigDecimal("119").compareTo(order3.getCheckoutPrice()));
    }

}
