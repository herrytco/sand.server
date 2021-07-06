package systems.nope.worldseed.repository.person;

import org.springframework.data.jpa.repository.JpaRepository;
import systems.nope.worldseed.model.person.PersonNote;

public interface PersonNoteRepository extends JpaRepository<PersonNote, Integer> {
}
