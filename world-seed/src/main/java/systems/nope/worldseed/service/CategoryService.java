package systems.nope.worldseed.service;

import org.springframework.stereotype.Service;
import systems.nope.worldseed.constants.CategoryConstants;
import systems.nope.worldseed.model.Category;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.repository.CategoryRepository;

import java.security.SecureRandom;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public String randomColor() {
        SecureRandom srng = new SecureRandom();

        int rand_num = srng.nextInt(0xffffff + 1);

        return String.format("#%06x", rand_num);
    }

    public void addDefaultCategories(World world) {
        for (int i = 0; i < CategoryConstants.defaultCategories.length; i++) {
            Category category = new Category(
                    world,
                    CategoryConstants.defaultCategories[i],
                    CategoryConstants.defaultCategoriesColors[i]
            );

            categoryRepository.save(category);
        }
    }
}
