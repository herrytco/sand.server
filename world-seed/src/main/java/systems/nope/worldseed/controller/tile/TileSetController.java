package systems.nope.worldseed.controller.tile;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import systems.nope.worldseed.dto.TilesetDto;
import systems.nope.worldseed.dto.request.AddTilesetRequest;
import systems.nope.worldseed.dto.request.UpdateTileRequest;
import systems.nope.worldseed.exception.FilesystemException;
import systems.nope.worldseed.model.tile.Tile;
import systems.nope.worldseed.model.tile.Tileset;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.service.tile.TilesetService;
import systems.nope.worldseed.service.WorldService;

import java.io.IOException;

@RestController
@RequestMapping("/tile-sets")
public class TileSetController {

    private final WorldService worldService;
    private final TilesetService tilesetService;

    public TileSetController(WorldService worldService, TilesetService tilesetService) {
        this.worldService = worldService;
        this.tilesetService = tilesetService;
    }

    @PutMapping("/{tilesetId}/tile/{tileId}")
    public void updatetile(
            @PathVariable int tileId,
            @PathVariable Integer tilesetId,
            @RequestBody UpdateTileRequest request
    ) {
        tilesetService.updateTile(tilesetId, tileId, request.getName(), request.getDescriptionShort(), request.getDescriptionLong(), request.getTextColor());
    }

    @GetMapping(
            value = "/{tilesetId}/tile/{tileId}/tile.png",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public @ResponseBody
    byte[] tile(
            @PathVariable Integer tilesetId,
            @PathVariable Integer tileId
    ) {
        return tilesetService.getTileImage(tileId);
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
