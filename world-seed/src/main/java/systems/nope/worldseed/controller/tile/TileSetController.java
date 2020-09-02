package systems.nope.worldseed.controller.tile;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
    ) {
        tilesetService.delete(setId);
    }

    @PostMapping("/worlds/{worldId}")
    public void add(
            @PathVariable Integer worldId,
            @RequestBody AddTilesetRequest request,
            @RequestParam(name = "image") MultipartFile tilesetImage
    ) {
        World world = worldService.get(worldId);

        Tileset tilesetNew = null;

        try {
            tilesetNew = tilesetService.add(world, request.getName(), request.getTileWidth(), request.getTileHeight());
            tileSetFileUtil.putFile(world, String.format("%d.png", tilesetNew.getId()), tilesetImage.getBytes());
        } catch (IOException e) {
            tilesetService.getTilesetRepository().delete(tilesetNew);
            throw new FilesystemException();
        }
    }
}
