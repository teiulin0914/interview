package tw.com.teiulin.pricecalc;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class Promotion5Test {

    /**
     * （加分題）訂單滿 X 元折 Z %，折扣每人只能總共優惠 N 元
     */
    @Test
    public void test() {
        // 訂單滿100元折10%，折扣每人只能總共優惠25元
        Calculator calculator =
                new Calculator(List.of(new Promotion5(new BigDecimal("100"), 10, new BigDecimal("25"))));

        User tim = new User(UUID.randomUUID().toString());
        User ann = new User(UUID.randomUUID().toString());

        // case 1: 未達折扣標準 - Tim買了1個99元的便當
        Order order1FromTim = tim.prepareOrder("20220105 12:30:01");
        order1FromTim.addProduct(new Product("TX-005", "便當", new BigDecimal("99")));

        calculator.execute(order1FromTim);

        Assert.assertEquals(1, order1FromTim.getProducts().size());
        Assert.assertEquals(0, new BigDecimal("99").compareTo(order1FromTim.getTotalPrice()));
        Assert.assertEquals(0, new BigDecimal("0").compareTo(order1FromTim.getDiscountPrice()));
        Assert.assertEquals(0, new BigDecimal("99").compareTo(order1FromTim.getCheckoutPrice()));


        // case 2: 剛好達折扣標準 - Tim買了1個100元的便當（Tim已使用10元折扣，剩15元折扣可用）
        Order order2FromTim = tim.prepareOrder("20220105 12:30:02");
        order2FromTim.addProduct(new Product("TX-005", "便當", new BigDecimal("100")));

        calculator.execute(order2FromTim);

        Assert.assertEquals(1, order2FromTim.getProducts().size());
        Assert.assertEquals(0, new BigDecimal("100").compareTo(order2FromTim.getTotalPrice()));
        Assert.assertEquals(0, new BigDecimal("10").compareTo(order2FromTim.getDiscountPrice()));
        Assert.assertEquals(0, new BigDecimal("90").compareTo(order2FromTim.getCheckoutPrice()));


        // case 3: 超過折扣標準 - Tim買了1個100元的便當、1條500元的香菸（Tim本次僅能折15元）
        Order order3FromTim = tim.prepareOrder("20220105 12:30:03");
        order3FromTim.addProduct(new Product("TX-005", "便當", new BigDecimal("100")));
        order3FromTim.addProduct(new Product("TX-006", "香菸(條)", new BigDecimal("500")));

        calculator.execute(order3FromTim);

        Assert.assertEquals(2, order3FromTim.getProducts().size());
        Assert.assertEquals(0, new BigDecimal("600").compareTo(order3FromTim.getTotalPrice()));
        Assert.assertEquals(0, new BigDecimal("15").compareTo(order3FromTim.getDiscountPrice()));
        Assert.assertEquals(0, new BigDecimal("585").compareTo(order3FromTim.getCheckoutPrice()));


        // case 4: 超過折扣標準 - Ann買了1條500元的香菸（Ann本次僅能折25元）
        Order order1FromAnn = ann.prepareOrder("20220105 12:30:04");
        order1FromAnn.addProduct(new Product("TX-006", "香菸(條)", new BigDecimal("500")));

        calculator.execute(order1FromAnn);

        Assert.assertEquals(1, order1FromAnn.getProducts().size());
        Assert.assertEquals(0, new BigDecimal("500").compareTo(order1FromAnn.getTotalPrice()));
        Assert.assertEquals(0, new BigDecimal("25").compareTo(order1FromAnn.getDiscountPrice()));
        Assert.assertEquals(0, new BigDecimal("475").compareTo(order1FromAnn.getCheckoutPrice()));
    }
}
