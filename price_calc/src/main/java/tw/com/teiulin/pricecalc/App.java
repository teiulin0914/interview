package tw.com.teiulin.pricecalc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

public class App {

    private static final Map<String, Product> productMap = new LinkedHashMap<>();
    private static final Calculator calculator;

    static {
        ObjectMapper jsonMapper = new ObjectMapper();

        try {
            List<Product> products = jsonMapper.readValue(App.class.getResourceAsStream("/products.json"), new TypeReference<>(){});

            for(int i = 0; i < products.size(); i++) {
                productMap.put(String.valueOf(i+1), products.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        Queue<Product> presents = new LinkedList<>();
        for(var i = 0; i < 2; i++) {
            presents.add(new Product("TX-000", "口香糖", BigDecimal.ZERO));
        }
        calculator = new Calculator(List.of(
                new Promotion1(0, "A", new BigDecimal("100"), 10),
                new Promotion2(0, null, List.of("TX-000"), Long.valueOf(2), new BigDecimal("6")),
                new Promotion3(1, null, new BigDecimal("100"), presents),
                new Promotion4(0, "B", new BigDecimal("100"), new BigDecimal("5"), Long.valueOf(1)),
                new Promotion5(1, "A", new BigDecimal("100"), 10, new BigDecimal("12")),
                new Promotion6(1, "B", new BigDecimal("100"), new BigDecimal("5"), new BigDecimal("9"))
        ));
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.print("請輸入您的大名: ");
        String name = in.next();
        System.out.print("歡迎光臨"+ name +"! 請問是否要購物呢? Y/N ");
        String isShopping = in.next();

        if("Y".equalsIgnoreCase(isShopping)) {
            User user = new User(name);
            Order order = user.prepareOrder(new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(new Date()));

            System.out.println("請輸入商品編號，若需要複選請以「,」隔開 ");
            productMap.forEach((s, product) -> {
                System.out.println(s +". "+ product.getName() +" $"+ product.getPrice());
            });

            String checkoutFlag = "N";
            do {
                String[] selected;
                if("N".equals(checkoutFlag)) {
                    selected = in.next().trim().split(",");
                } else {
                    selected = checkoutFlag.trim().split(",");
                }

                Arrays.stream(selected).forEach(k -> {
                    Product product = productMap.get(k);
                    if(product != null) {
                        order.addProduct(product);
                    }
                });

                System.out.print("若繼續購買，請輸入商品編號。若要結帳請輸入Y ");
                checkoutFlag = in.next();

            } while(!"Y".equalsIgnoreCase(checkoutFlag));

            calculator.execute(order);

            System.out.println("");
            System.out.println("您購買了:");
            for(var product : order.getProducts()) {
                System.out.println(product.getName() +" $"+ product.getPrice());
            }
            System.out.println("-----");

            System.out.println("原始金額: $"+ order.getTotalPrice());
            System.out.println("折扣金額: $"+ order.getDiscountPrice());
            System.out.println("結帳金額: $"+ order.getCheckoutPrice());
            System.out.println("");
        }

        System.out.println("謝謝光臨!");
    }
}
