package systems.nope.worldseed.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import systems.nope.worldseed.model.Role;
import systems.nope.worldseed.repository.RoleRepository;

import java.util.List;

@RestController
@RequestMapping("world-roles")
public class RoleController {
    private final RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public List<Role> all() {
        return roleRepository.findAll();
    }
}
