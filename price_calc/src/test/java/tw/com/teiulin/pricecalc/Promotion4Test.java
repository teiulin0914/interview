package tw.com.teiulin.pricecalc;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class Promotion4Test {

    /**
     * 訂單滿 X 元折 Y 元，此折扣在全站總共只能套用 N 次
     */
    @Test
    public void test() {
        // 訂單滿100元折5元，此折扣在全站總共只能套用2次
        Calculator calculator =
                new Calculator(List.of(new Promotion4(new BigDecimal("100"), new BigDecimal("5"), Long.valueOf(2))));

        User tim = new User(UUID.randomUUID().toString());
        User ann = new User(UUID.randomUUID().toString());
        User ben = new User(UUID.randomUUID().toString());

        // case 1: 未達折扣標準 - Tim買了1個99元的便當
        Order order1FromTim = tim.prepareOrder("20220104 12:30:01");
        order1FromTim.addProduct(new Product("TX-005", "便當", new BigDecimal("99")));

        calculator.execute(order1FromTim);

        Assert.assertEquals(1, order1FromTim.getProducts().size());
        Assert.assertEquals(0, new BigDecimal("99").compareTo(order1FromTim.getTotalPrice()));
        Assert.assertEquals(0, new BigDecimal("0").compareTo(order1FromTim.getDiscountPrice()));
        Assert.assertEquals(0, new BigDecimal("99").compareTo(order1FromTim.getCheckoutPrice()));


        // case 2: 剛好達折扣標準 - Tim買了1個100元的便當
        Order order2FromTim = tim.prepareOrder("20220104 12:30:02");
        order2FromTim.addProduct(new Product("TX-005", "便當", new BigDecimal("100")));

        calculator.execute(order2FromTim);

        Assert.assertEquals(1, order2FromTim.getProducts().size());
        Assert.assertEquals(0, new BigDecimal("100").compareTo(order2FromTim.getTotalPrice()));
        Assert.assertEquals(0, new BigDecimal("5").compareTo(order2FromTim.getDiscountPrice()));
        Assert.assertEquals(0, new BigDecimal("95").compareTo(order2FromTim.getCheckoutPrice()));


        // case 3: 超過折扣標準 - Ann買了1個101元的便當
        Order order1FromAnn = ann.prepareOrder("20220104 12:30:03");
        order1FromAnn.addProduct(new Product("TX-005", "便當", new BigDecimal("101")));

        calculator.execute(order1FromAnn);

        Assert.assertEquals(1, order1FromAnn.getProducts().size());
        Assert.assertEquals(0, new BigDecimal("101").compareTo(order1FromAnn.getTotalPrice()));
        Assert.assertEquals(0, new BigDecimal("5").compareTo(order1FromAnn.getDiscountPrice()));
        Assert.assertEquals(0, new BigDecimal("96").compareTo(order1FromAnn.getCheckoutPrice()));


        // case 4: 剛好達折扣標準，但優惠已達上限 - Ben買了1個100元的便當
        Order order1FromBen = ben.prepareOrder("20220104 12:30:04");
        order1FromBen.addProduct(new Product("TX-005", "便當", new BigDecimal("100")));

        calculator.execute(order1FromBen);

        Assert.assertEquals(1, order1FromBen.getProducts().size());
        Assert.assertEquals(0, new BigDecimal("100").compareTo(order1FromBen.getTotalPrice()));
        Assert.assertEquals(0, new BigDecimal("0").compareTo(order1FromBen.getDiscountPrice()));
        Assert.assertEquals(0, new BigDecimal("100").compareTo(order1FromBen.getCheckoutPrice()));
    }
}
