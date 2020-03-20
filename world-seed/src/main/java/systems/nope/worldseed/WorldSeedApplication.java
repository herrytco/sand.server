package systems.nope.worldseed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
public class WorldSeedApplication {
    public static void main(String[] args) {
        SpringApplication.run(WorldSeedApplication.class, args);
    }
}
