package systems.nope.worldseed;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/echo")
public class EchoController {

    @GetMapping
    public String hello() {
        return "Hello There,\nGeneral Kenobi!";
    }
}
