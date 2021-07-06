package systems.nope.worldseed.person;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import systems.nope.worldseed.model.person.Person;
import systems.nope.worldseed.model.stat.StatSheet;
import systems.nope.worldseed.service.StatSheetService;
import systems.nope.worldseed.service.person.PersonService;
import systems.nope.worldseed.util.StatUtils;
import systems.nope.worldseed.util.data.DataStructure;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomTest {

    @Autowired
    private PersonService personService;

    @Autowired
    private StatSheetService statSheetService;

    @Autowired
    private StatUtils statUtils;

    @Test
    public void customTest1() {
        Optional<Person> personOptional = personService.findByApiKey("wqegdwqepocttgquzojdgfqmzgibuaglnfqqtmthwdmwezkcuuolatmwlbtfmufxzwgqxzpzcekjjwajvtrjzthzqxumtsfepxunqjhdnxgwvldljquogiacqpywswfspwkcxytyonesncaxckafqghjkxbvneyovjqokcirxvcolzjlwvhaffnxlxjpclrhjeqwifnwknfzaassdqyehbwkgglcoxjrfnqycjfwjoaibdbcwjjraynnhwjejewl");

        if(personOptional.isEmpty()) {
            System.out.println("No person found, aborting...");
            return;
        }

        Person person = personOptional.get();
        StatSheet sheet = statSheetService.get(1513);

        List<DataStructure<StatSheet>> forest = statUtils.constructSheetForest(person.getStatSheets());
    }
}
