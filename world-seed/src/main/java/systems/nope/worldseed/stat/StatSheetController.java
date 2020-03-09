package systems.nope.worldseed.stat;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/stat-sheets")
public class StatSheetController {
    private final StatSheetRepository statSheetRepository;

    public StatSheetController(StatSheetRepository statSheetRepository) {
        this.statSheetRepository = statSheetRepository;
    }

    @GetMapping
    public List<StatSheet> all() {
        return statSheetRepository.findAll();
    }
}

