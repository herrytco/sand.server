package systems.nope.worldseed.dto.person;

import lombok.Data;
import systems.nope.worldseed.model.person.PersonNote;

@Data
public class PersonNoteDto {
    private Integer id;
    private String content;

    public static PersonNoteDto fromNote(PersonNote note) {
        PersonNoteDto result = new PersonNoteDto();

        result.setId(note.getId());
        result.setContent(note.getContent());

        return result;
    }
}
