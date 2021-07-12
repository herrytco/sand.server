package systems.nope.worldseed.util;

import org.springframework.stereotype.Service;
import systems.nope.worldseed.dto.UserWorldRoleDto;
import systems.nope.worldseed.dto.WorldDto;
import systems.nope.worldseed.exception.DataMissmatchException;
import systems.nope.worldseed.model.User;
import systems.nope.worldseed.model.UserWorldRole;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.model.person.Person;
import systems.nope.worldseed.model.stat.StatSheet;

import java.util.LinkedList;
import java.util.stream.Collectors;

@Service
public class WorldDtoFactory {

    public WorldDto from(World world) {
        return new WorldDto(
                world.getId(),
                world.getName(),
                world.getDescription(),
                world.getPersons() == null
                        ? new LinkedList<>()
                        : world.getPersons().stream().map(Person::getId).collect(Collectors.toList()),
                world.getSheets() == null
                        ? new LinkedList<>()
                        : world.getSheets().stream().map(StatSheet::getId).collect(Collectors.toList()),
                world.getWorldUsers() == null
                        ? new LinkedList<>()
                        : world.getWorldUsers().stream().map(UserWorldRoleDto::fromUserWorldRole).collect(Collectors.toList()),
                world.getSeed()
        );
    }

    public WorldDto from(World world, User user) throws DataMissmatchException {
        UserWorldRole role = null;

        for (UserWorldRole r2 : world.getWorldUsers()) {
            if (r2.getWorld().getId() == world.getId() && r2.getUser().getId() == user.getId())
                role = r2;
        }

        if (role == null)
            throw new DataMissmatchException("You are not allowed to view this world!");

        if (role.getRole().getName().equals("Owner"))
            return from(world);

        WorldDto dto = new WorldDto(world.getId(), world.getName(), world.getDescription(), world.getSeed());

        LinkedList<UserWorldRoleDto> userWorldRoleDtos = new LinkedList<>();
        userWorldRoleDtos.add(UserWorldRoleDto.fromUserWorldRole(role));


        dto.setPersons(user.getPersons() != null ? user.getPersons().stream().map(Person::getId).collect(Collectors.toList()) : new LinkedList<>());
        dto.setSheets(world.getSheets() != null
                ? world.getSheets().stream().map(StatSheet::getId).collect(Collectors.toList())
                : new LinkedList<>());
        dto.setJoinedUsers(userWorldRoleDtos);

        return dto;
    }


}
