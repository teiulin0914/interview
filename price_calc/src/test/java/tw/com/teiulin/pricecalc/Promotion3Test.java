package tw.com.teiulin.pricecalc;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

public class Promotion3Test {

    /**
     * 訂單滿 X 元贈送特定商品
     */
    @Test
    public void test() {
        // 訂單滿100元贈送口香糖(庫存2個)
        Queue<Product> presents = new LinkedList<>();
        for(var i = 0; i < 2; i++) {
            presents.add(new Product("TX-000", "口香糖", BigDecimal.ZERO));
        }
        Calculator calculator =
                new Calculator(List.of(new Promotion3(new BigDecimal("100"), presents)));

        User user = new User(UUID.randomUUID().toString());

        // case 1: 未達折扣標準
        Order order = user.prepareOrder("20220103 12:30:01");
        order.addProduct(new Product("TX-002", "洋芋片", new BigDecimal("50")));
        order.addProduct(new Product("TX-004", "啤酒", new BigDecimal("49")));

        calculator.execute(order);

        Assert.assertEquals(2, order.getProducts().size());
        Assert.assertEquals(0, new BigDecimal("99").compareTo(order.getTotalPrice()));
        Assert.assertEquals(0, new BigDecimal("0").compareTo(order.getDiscountPrice()));
        Assert.assertEquals(0, new BigDecimal("99").compareTo(order.getCheckoutPrice()));


        // case 2: 剛好達折扣標準 (購買數加上贈品，總商品數為3)
        Order order2 = user.prepareOrder("20220103 12:30:02");
        order2.addProduct(new Product("TX-002", "洋芋片", new BigDecimal("50")));
        order2.addProduct(new Product("TX-004", "啤酒", new BigDecimal("50")));

        calculator.execute(order2);

        Assert.assertEquals(3, order2.getProducts().size());
        Assert.assertEquals(0, new BigDecimal("100").compareTo(order2.getTotalPrice()));
        Assert.assertEquals(0, new BigDecimal("0").compareTo(order2.getDiscountPrice()));
        Assert.assertEquals(0, new BigDecimal("100").compareTo(order2.getCheckoutPrice()));


        // case 3: 超過折扣標準 (購買數加上贈品，總商品數為3)
        Order order3 = user.prepareOrder("20220103 12:30:03");
        order3.addProduct(new Product("TX-002", "洋芋片", new BigDecimal("50")));
        order3.addProduct(new Product("TX-004", "啤酒", new BigDecimal("51")));

        calculator.execute(order3);

        Assert.assertEquals(3, order3.getProducts().size());
        Assert.assertEquals(0, new BigDecimal("101").compareTo(order3.getTotalPrice()));
        Assert.assertEquals(0, new BigDecimal("0").compareTo(order3.getDiscountPrice()));
        Assert.assertEquals(0, new BigDecimal("101").compareTo(order3.getCheckoutPrice()));


        // case 4: 達折扣標準，但已無贈品庫存
        Order order4 = user.prepareOrder("20220103 12:30:04");
        order4.addProduct(new Product("TX-002", "洋芋片", new BigDecimal("50")));
        order4.addProduct(new Product("TX-004", "啤酒", new BigDecimal("50")));

        calculator.execute(order4);

        Assert.assertEquals(2, order4.getProducts().size());
        Assert.assertEquals(0, new BigDecimal("100").compareTo(order4.getTotalPrice()));
        Assert.assertEquals(0, new BigDecimal("0").compareTo(order4.getDiscountPrice()));
        Assert.assertEquals(0, new BigDecimal("100").compareTo(order4.getCheckoutPrice()));
    }

}
