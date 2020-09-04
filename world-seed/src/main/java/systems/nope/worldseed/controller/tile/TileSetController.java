package systems.nope.worldseed.controller.tile;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import systems.nope.worldseed.dto.TilesetDto;
import systems.nope.worldseed.dto.request.AddTilesetRequest;
import systems.nope.worldseed.exception.FilesystemException;
import systems.nope.worldseed.model.Tileset;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.service.TilesetService;
import systems.nope.worldseed.service.WorldService;
import systems.nope.worldseed.util.file.TileSetFileUtil;

import java.io.IOException;

@RestController
@RequestMapping("/tile-sets")
public class TileSetController {

    private final TileSetFileUtil tileSetFileUtil;
    private final WorldService worldService;
    private final TilesetService tilesetService;

    public TileSetController(TileSetFileUtil tileSetFileUtil, WorldService worldService, TilesetService tilesetService) {
        this.tileSetFileUtil = tileSetFileUtil;
        this.worldService = worldService;
        this.tilesetService = tilesetService;
    }

    @DeleteMapping("/id/{setId}")
    public void delete(
            @PathVariable Integer setId
    ) throws IOException {
        tilesetService.delete(setId);
    }

    @PostMapping("/worlds/{worldId}")
    public TilesetDto add(
            @PathVariable Integer worldId,
            @RequestBody AddTilesetRequest request,
            @RequestParam(name = "image") MultipartFile tilesetImage
    ) {
        World world = worldService.get(worldId);

        try {
            Tileset tilesetNew = tilesetService.add(world, request.getName(), request.getTileWidth(), request.getTileHeight(), tilesetImage.getBytes());
            return TilesetDto.fromTileset(tilesetNew);
        } catch (IOException e) {
            throw new FilesystemException();
        }
    }
}
