package politetransaction.store.gateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class GatewayResource{
    @GetMapping("/hello")
    public String info(){
        return "Hello, this is the gateway service!";
    }
}
