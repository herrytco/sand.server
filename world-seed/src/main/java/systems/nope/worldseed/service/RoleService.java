package systems.nope.worldseed.service;

import org.springframework.stereotype.Service;
import systems.nope.worldseed.model.Role;
import systems.nope.worldseed.model.RoleType;
import systems.nope.worldseed.repository.RoleRepository;
import systems.nope.worldseed.constants.RoleConstants;

import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    private String getRoleName(RoleType type) {
        switch (type) {
            case Owner:
                return RoleConstants.roleOwner;

            case Player:
                return RoleConstants.rolePlayer;

            case Visitor:
                return RoleConstants.roleVisitor;

            default:
                throw new IllegalArgumentException(String.format("No Roletype found for type %s", type));
        }
    }

    public Role getRoleForType(RoleType type) {
        Optional<Role> optionalRole = roleRepository.findByName(getRoleName(type));

        if (optionalRole.isEmpty()) {
            Role role = new Role(getRoleName(type));
            roleRepository.save(role);
            optionalRole = roleRepository.findByName(getRoleName(type));
        }

        if (optionalRole.isPresent())
            return optionalRole.get();

        throw new IllegalStateException(String.format("Role %s not be present, insertion failed.", getRoleName(type)));
    }
}
