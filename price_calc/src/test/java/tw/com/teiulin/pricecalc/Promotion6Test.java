package tw.com.teiulin.pricecalc;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class Promotion6Test {

    /**
     * （加分題）訂單滿 X 元折 Y 元，此折扣在全站每個月折扣上限為 N 元
     */
    @Test
    public void test() {
        // 訂單滿100元折20元，此折扣在全站每個月折扣上限為50元
        Calculator calculator = new Calculator(List.of(new Promotion6(new BigDecimal("100"),
                new BigDecimal("20"), new BigDecimal("50"))));

        User tim = new User(UUID.randomUUID().toString());
        User ann = new User(UUID.randomUUID().toString());

        // case 1: 未達折扣標準 - Tim在2022年一月六日 買了1個99元的便當
        Order order1FromTim = tim.prepareOrder("20220106 12:30:01");
        order1FromTim.addProduct(new Product("TX-005", "便當", new BigDecimal("99")));

        calculator.execute(order1FromTim);

        Assert.assertEquals(1, order1FromTim.getProducts().size());
        Assert.assertEquals(0, new BigDecimal("99").compareTo(order1FromTim.getTotalPrice()));
        Assert.assertEquals(0, new BigDecimal("0").compareTo(order1FromTim.getDiscountPrice()));
        Assert.assertEquals(0, new BigDecimal("99").compareTo(order1FromTim.getCheckoutPrice()));


        // case 2: 剛好達折扣標準 - Tim在2022年一月七日 買了1個100元的便當(一月折扣剩30元)
        Order order2FromTim = tim.prepareOrder("20220107 12:30:01");
        order2FromTim.addProduct(new Product("TX-005", "便當", new BigDecimal("100")));

        calculator.execute(order2FromTim);

        Assert.assertEquals(1, order2FromTim.getProducts().size());
        Assert.assertEquals(0, new BigDecimal("100").compareTo(order2FromTim.getTotalPrice()));
        Assert.assertEquals(0, new BigDecimal("20").compareTo(order2FromTim.getDiscountPrice()));
        Assert.assertEquals(0, new BigDecimal("80").compareTo(order2FromTim.getCheckoutPrice()));


        // case 3: 超過折扣標準 - Ann在2022年一月八日 買了1條500元的香菸(一月折扣剩10元)
        Order order1FromAnn = ann.prepareOrder("20220108 12:30:01");
        order1FromAnn.addProduct(new Product("TX-006", "香菸(條)", new BigDecimal("500")));

        calculator.execute(order1FromAnn);

        Assert.assertEquals(1, order1FromAnn.getProducts().size());
        Assert.assertEquals(0, new BigDecimal("500").compareTo(order1FromAnn.getTotalPrice()));
        Assert.assertEquals(0, new BigDecimal("20").compareTo(order1FromAnn.getDiscountPrice()));
        Assert.assertEquals(0, new BigDecimal("480").compareTo(order1FromAnn.getCheckoutPrice()));


        // case 4: 剛好達折扣標準 - Ann在2022年一月九日 買了1個100元的便當(僅折10元)
        Order order2FromAnn = ann.prepareOrder("20220109 12:30:01");
        order2FromAnn.addProduct(new Product("TX-005", "便當", new BigDecimal("100")));

        calculator.execute(order2FromAnn);

        Assert.assertEquals(1, order2FromAnn.getProducts().size());
        Assert.assertEquals(0, new BigDecimal("100").compareTo(order2FromAnn.getTotalPrice()));
        Assert.assertEquals(0, new BigDecimal("10").compareTo(order2FromAnn.getDiscountPrice()));
        Assert.assertEquals(0, new BigDecimal("90").compareTo(order2FromAnn.getCheckoutPrice()));


        // case 5: 剛好達折扣標準 - Ann在2022年二月一日 買了1個100元的便當(折扣上限重置，折20元)
        Order order3FromAnn = ann.prepareOrder("20220201 12:30:01");
        order3FromAnn.addProduct(new Product("TX-005", "便當", new BigDecimal("100")));

        calculator.execute(order3FromAnn);

        Assert.assertEquals(1, order3FromAnn.getProducts().size());
        Assert.assertEquals(0, new BigDecimal("100").compareTo(order3FromAnn.getTotalPrice()));
        Assert.assertEquals(0, new BigDecimal("20").compareTo(order3FromAnn.getDiscountPrice()));
        Assert.assertEquals(0, new BigDecimal("80").compareTo(order3FromAnn.getCheckoutPrice()));
    }
}
