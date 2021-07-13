package systems.nope.worldseed.dto.person;

import lombok.Data;
import systems.nope.worldseed.model.person.Person;
import systems.nope.worldseed.model.item.Item;
import systems.nope.worldseed.model.person.PersonNote;
import systems.nope.worldseed.model.stat.instance.StatValueInstance;
import systems.nope.worldseed.model.stat.StatSheet;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PersonDto {
    private final int id;

    private final int world;

    private final String name;

    private final String apiKey;

    private final List<Integer> sheets;

    private final List<Integer> stats;

    private final List<Integer> items;

    private final List<Integer> notes;

    private final Integer controllingUserId;

    private final String portrait;

    private PersonDto(int id, int world, String name, String apiKey, List<Integer> sheets, List<Integer> stats,
                      List<Integer> items, List<Integer> notes, Integer controllingUserId, String portrait) {
        this.id = id;
        this.world = world;
        this.name = name;
        this.apiKey = apiKey;
        this.sheets = sheets;
        this.stats = stats;
        this.items = items;
        this.notes = notes;
        this.controllingUserId = controllingUserId;
        this.portrait = portrait;
    }

    public static PersonDto fromPerson(Person person) {
        return new PersonDto(
                person.getId(),
                person.getWorld().getId(),
                person.getName(),
                person.getApiKey(),
                person.getStatSheets() != null ? person.getStatSheets().stream().map(StatSheet::getId).collect(Collectors.toList()) : new LinkedList<>(),
                person.getStatValues() != null ? person.getStatValues().stream().map(StatValueInstance::getId).collect(Collectors.toList()) : new LinkedList<>(),
                person.getItems() != null ? person.getItems().stream().map(Item::getId).collect(Collectors.toList()) : new LinkedList<>(),
                person.getNotes() != null ? person.getNotes().stream().map(PersonNote::getId).collect(Collectors.toList()) : new LinkedList<>(),
                person.getControllingUser() != null ? person.getControllingUser().getId() : null,
                person.getPortraitImage()
        );
    }
}
