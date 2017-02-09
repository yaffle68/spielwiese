package demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@RestController
public class Application {

    @Value("${info.version}")
    private String version;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @RequestMapping("/hello")
    public String home() {
        return "Hello World";
    }

    @RequestMapping("/version")
    public String getVersion() {
        return version;
    }

    @RequestMapping("/resource")
    public Map<String, Object> resource() {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("id", UUID.randomUUID().toString());
        model.put("content", "This is the content");
        return model;
    }

    @RequestMapping(path = "/stockprices", method = RequestMethod.GET)
    public List<String> stockPrices() {
        return Arrays.asList("One", "Two", "Three");
    }

//
//    /**
//     * Escape an html string. Escaping data received from the client helps to
//     * prevent cross-site script vulnerabilities.
//     *
//     * @param html the html string to escape
//     * @return the escaped string
//     */
//    private String escapeHtml(String html) {
//        if (html == null) {
//            return null;
//        }
//        return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
//    }
//
//    @Override
//    public StockPrice[] getPrices(String[] symbols) throws DelistedException {
//        List<StockPrice> prices = new ArrayList<StockPrice>(symbols.length);
//
//        Random rnd = new Random();
//        for (String nextSymbol : symbols) {
//            if ("ERR".equals(nextSymbol)) {
//                throw new DelistedException(nextSymbol);
//            }
//            double price = rnd.nextDouble() * MAX_PRICE;
//            double change = price * MAX_PRICE_CHANGE * (rnd.nextDouble() * 2.0 - 1.0);
//
//            prices.add(new StockPrice(nextSymbol, price, change));
//        }
//
//        return prices.toArray(new StockPrice[]{});
//    }

}
