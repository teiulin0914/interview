package tw.com.teiulin.pricecalc;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

public class PromotionCombineTest {

    /**
     * 訂單滿 X 元折 Z %
     * 特定商品滿 X 件折 Y 元
     * 訂單滿 X 元贈送特定商品
     * 訂單滿 X 元折 Y 元，此折扣在全站總共只能套用 N 次
     */
    @Test
    public void testNormal() {
        Queue<Product> presents = new LinkedList<>();
        for(var i = 0; i < 2; i++) {
            presents.add(new Product("TX-000", "口香糖", BigDecimal.ZERO));
        }

        /*
        <1> 訂單滿100元折10% (優先於<5>, 並利用獨佔標識排除掉<5>)
        <2> 口香糖滿2件折6元 (優先於<3>, 避免贈品影響折扣金額)
        <3> 訂單滿100元送口香糖
        <4> 訂單滿100元折5元，此折扣在全站總共只能套用1次 (優先於<6>, 並利用獨佔標識排除掉<6>)
        <5> 訂單滿100元折10%，折扣每人只能總共優惠12元 (不作用)
        <6> 訂單滿100元折5元，此折扣在全站每個月折扣上限為9元 (不作用)
         */
        Calculator calculator = new Calculator(List.of(
                new Promotion1(0, "A", new BigDecimal("100"), 10),
                new Promotion2(0, null, List.of("TX-000"), Long.valueOf(2), new BigDecimal("6")),
                new Promotion3(1, null, new BigDecimal("100"), presents),
                new Promotion4(0, "B", new BigDecimal("100"), new BigDecimal("5"), Long.valueOf(1)),
                new Promotion5(1, "A", new BigDecimal("100"), 10, new BigDecimal("12")),
                new Promotion6(1, "B", new BigDecimal("100"), new BigDecimal("5"), new BigDecimal("9"))
        ));

        User tim = new User(UUID.randomUUID().toString());
        User ann = new User(UUID.randomUUID().toString());

        /*
        case 1: Tim買了一個口香糖10元，一罐啤酒90元，共100元。
                觸發折10%、贈品、折5元
         */
        Order order1FromTim = tim.prepareOrder("20220101 12:30:01");
        order1FromTim.addProduct(new Product("TX-000", "口香糖", new BigDecimal("10")));
        order1FromTim.addProduct(new Product("TX-004", "啤酒", new BigDecimal("90")));

        calculator.execute(order1FromTim);

        Assert.assertEquals(3, order1FromTim.getProducts().size());
        Assert.assertEquals(0, new BigDecimal("100").compareTo(order1FromTim.getTotalPrice()));
        Assert.assertEquals(0, new BigDecimal("15").compareTo(order1FromTim.getDiscountPrice()));
        Assert.assertEquals(0, new BigDecimal("85").compareTo(order1FromTim.getCheckoutPrice()));


        /*
        case 2: Ann買了兩個口香糖20元，一罐啤酒90元，共110元。
                觸發折10%、折6元、贈品
         */
        Order order1FromAnn = ann.prepareOrder("20220101 12:30:02");
        order1FromAnn.addProduct(new Product("TX-000", "口香糖", new BigDecimal("10")));
        order1FromAnn.addProduct(new Product("TX-000", "口香糖", new BigDecimal("10")));
        order1FromAnn.addProduct(new Product("TX-004", "啤酒", new BigDecimal("90")));

        calculator.execute(order1FromAnn);

        Assert.assertEquals(4, order1FromAnn.getProducts().size());
        Assert.assertEquals(0, new BigDecimal("110").compareTo(order1FromAnn.getTotalPrice()));
        Assert.assertEquals(0, new BigDecimal("17").compareTo(order1FromAnn.getDiscountPrice()));
        Assert.assertEquals(0, new BigDecimal("93").compareTo(order1FromAnn.getCheckoutPrice()));
    }

    /**
     * 特定商品滿 X 件折 Y 元
     * 訂單滿 X 元贈送特定商品
     * （加分題）訂單滿 X 元折 Z %，折扣每人只能總共優惠 N 元
     * （加分題）訂單滿 X 元折 Y 元，此折扣在全站每個月折扣上限為 N 元
     */
    @Test
    public void testBonus() {
        Queue<Product> presents = new LinkedList<>();
        for(var i = 0; i < 2; i++) {
            presents.add(new Product("TX-000", "口香糖", BigDecimal.ZERO));
        }

        /*
        <1> 訂單滿100元折10% (不作用)
        <2> 口香糖滿2件折6元 (優先於<3>, 避免贈品影響折扣金額)
        <3> 訂單滿100元送口香糖
        <4> 訂單滿100元折5元，此折扣在全站總共只能套用1次 (不作用)
        <5> 訂單滿100元折10%，折扣每人只能總共優惠12元 (優先於<1>, 並利用獨佔標識排除掉<1>)
        <6> 訂單滿100元折5元，此折扣在全站每個月折扣上限為9元 (優先於<4>, 並利用獨佔標識排除掉<4>)
         */
        Calculator calculator = new Calculator(List.of(
                new Promotion1(1, "A", new BigDecimal("100"), 10),
                new Promotion2(0, null, List.of("TX-000"), Long.valueOf(2), new BigDecimal("6")),
                new Promotion3(1, null, new BigDecimal("100"), presents),
                new Promotion4(1, "B", new BigDecimal("100"), new BigDecimal("5"), Long.valueOf(1)),
                new Promotion5(0, "A", new BigDecimal("100"), 10, new BigDecimal("12")),
                new Promotion6(0, "B", new BigDecimal("100"), new BigDecimal("5"), new BigDecimal("9"))
        ));

        User tim = new User(UUID.randomUUID().toString());
        User ann = new User(UUID.randomUUID().toString());

        /*
        case 1: Tim買了一個口香糖10元，一罐啤酒90元，一包洋芋片50元，共150元。
                觸發折10%(上限12元)、贈品、折5元
         */
        Order order1FromTim = tim.prepareOrder("20220101 12:30:01");
        order1FromTim.addProduct(new Product("TX-000", "口香糖", new BigDecimal("10")));
        order1FromTim.addProduct(new Product("TX-004", "啤酒", new BigDecimal("90")));
        order1FromTim.addProduct(new Product("TX-002", "洋芋片", new BigDecimal("50")));

        calculator.execute(order1FromTim);

        Assert.assertEquals(4, order1FromTim.getProducts().size());
        Assert.assertEquals(0, new BigDecimal("150").compareTo(order1FromTim.getTotalPrice()));
        Assert.assertEquals(0, new BigDecimal("17").compareTo(order1FromTim.getDiscountPrice()));
        Assert.assertEquals(0, new BigDecimal("133").compareTo(order1FromTim.getCheckoutPrice()));


        /*
        case 2: Ann買了兩個口香糖20元，一罐啤酒90元，共110元。
                觸發折10%、折6元、贈品、折4元(9-5)
         */
        Order order1FromAnn = ann.prepareOrder("20220101 12:30:02");
        order1FromAnn.addProduct(new Product("TX-000", "口香糖", new BigDecimal("10")));
        order1FromAnn.addProduct(new Product("TX-000", "口香糖", new BigDecimal("10")));
        order1FromAnn.addProduct(new Product("TX-004", "啤酒", new BigDecimal("90")));

        calculator.execute(order1FromAnn);

        Assert.assertEquals(4, order1FromAnn.getProducts().size());
        Assert.assertEquals(0, new BigDecimal("110").compareTo(order1FromAnn.getTotalPrice()));
        Assert.assertEquals(0, new BigDecimal("21").compareTo(order1FromAnn.getDiscountPrice()));
        Assert.assertEquals(0, new BigDecimal("89").compareTo(order1FromAnn.getCheckoutPrice()));
    }

}
