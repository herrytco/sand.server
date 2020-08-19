package systems.nope.worldseed.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import systems.nope.worldseed.model.Skill;

public interface SkillRepository extends JpaRepository<Skill, Integer> {
}
