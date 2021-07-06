package systems.nope.worldseed.service;

import org.springframework.stereotype.Service;
import systems.nope.worldseed.exception.NotFoundException;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.model.stat.instance.StatValueInstance;
import systems.nope.worldseed.model.stat.instance.StatValueInstanceConstant;
import systems.nope.worldseed.model.stat.instance.StatValueInstanceSynthesized;
import systems.nope.worldseed.model.stat.value.StatValue;
import systems.nope.worldseed.repository.stat.StatValueInstanceConstantRepository;
import systems.nope.worldseed.repository.stat.StatValueInstanceSynthesizedRepository;

import java.util.Optional;

@Service
public class StatValueInstanceService {
    private final StatValueInstanceConstantRepository statValueInstanceConstantRepository;
    private final StatValueInstanceSynthesizedRepository statValueInstanceSynthesizedRepository;

    public StatValueInstanceService(StatValueInstanceConstantRepository statValueInstanceConstantRepository, StatValueInstanceSynthesizedRepository statValueInstanceSynthesizedRepository) {
        this.statValueInstanceConstantRepository = statValueInstanceConstantRepository;
        this.statValueInstanceSynthesizedRepository = statValueInstanceSynthesizedRepository;
    }

    public StatValueInstanceConstant getConstant(Integer id) {
        Optional<StatValueInstanceConstant> optionalStatValueInstanceConstant = findConstant(id);

        if (optionalStatValueInstanceConstant.isEmpty())
            throw new NotFoundException(id);

        return optionalStatValueInstanceConstant.get();
    }

    public Optional<StatValueInstanceConstant> findConstant(Integer id) {
        return statValueInstanceConstantRepository.findById(id);
    }

    public StatValueInstanceSynthesized getSynthesized(Integer id) {
        Optional<StatValueInstanceSynthesized> optionalStatValueInstanceSynthesized = findSynthesized(id);

        if (optionalStatValueInstanceSynthesized.isEmpty())
            throw new NotFoundException(id);

        return optionalStatValueInstanceSynthesized.get();
    }

    public Optional<StatValueInstanceSynthesized> findSynthesized(Integer id) {
        return statValueInstanceSynthesizedRepository.findById(id);
    }

    public StatValueInstanceConstant add(World world, StatValue stat, Integer initialValue) {
        StatValueInstanceConstant statNew = new StatValueInstanceConstant(
                world, stat, initialValue
        );

        statValueInstanceConstantRepository.save(statNew);

        return statNew;
    }

    public StatValueInstanceSynthesized add(World world, StatValue stat) {
        StatValueInstanceSynthesized statNew = new StatValueInstanceSynthesized(
                world, stat
        );

        statValueInstanceSynthesizedRepository.save(statNew);

        return statNew;
    }

    public void update(StatValueInstanceConstant stat, Integer value) {
        stat.setValue(value);
        statValueInstanceConstantRepository.save(stat);
    }
}
