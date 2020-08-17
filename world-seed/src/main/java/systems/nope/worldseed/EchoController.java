package systems.nope.worldseed;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/echo")
public class EchoController {

    @GetMapping("/ip")
    public Map<String, Object> echo(HttpServletRequest request) {
        Map<String, Object> data = new HashMap<>();

        data.put("ip", request.getRemoteAddr());
        data.put("host", request.getRemoteHost());
        data.put("port", request.getRemotePort());

        return data;
    }

    @GetMapping
    public String hello() {
        return "Hello There,\nGeneral Kenobi!";
    }
}
