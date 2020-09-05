package systems.nope.worldseed.util.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import systems.nope.worldseed.model.World;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

public abstract class FileUtil {
    final String root;

    private final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public FileUtil(String root) {
        this.root = root;
    }

    public File root() {
        File root = new File("_seed-data/");

        if (!root.exists()) {
            if (root.mkdirs())
                logger.info(String.format("Created folder '%s'", root.getAbsolutePath()));
        }

        File subRoot = new File(root.getAbsolutePath() + "/" + this.root);

        if (!subRoot.exists()) {
            if (subRoot.mkdirs())
                logger.info(String.format("Created folder '%s'", subRoot.getAbsolutePath()));
        }

        return subRoot;
    }

    public void deleteDirectory(World world, String path) throws IOException {
        File directoryToBeDeleted = new File(String.format("%s/%d/%s", root().getAbsolutePath(), world.getId(), path));

        Files.walk(directoryToBeDeleted.toPath())
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

    public File resolve(World world, String path) throws IOException {
        File target = new File(String.format("%s/%s", resolveWorldFolder(world).getAbsolutePath(), path));

        File parent = new File(target.getParent());

        if (!parent.exists() && parent.mkdirs())
            logger.info(String.format("Created directory '%s'.", parent.getAbsolutePath()));

        if (!target.exists() && target.createNewFile())
            logger.info(String.format("Created file '%s'.", target.getAbsolutePath()));

        return target;
    }

    public File resolveWorldFolder(World world) {
        File target = new File(String.format("%s/%d", root().getAbsolutePath(), world.getId()));

        if (!target.exists() && target.mkdirs())
            logger.info(String.format("Created directory '%s'", target.getAbsolutePath()));

        return target;
    }

    public void deleteFile(World world, String path) throws IOException {
        File target = resolve(world, path);

        if (target.exists() && target.delete())
            logger.info(String.format("Deleted file '%s'.", target.getAbsolutePath()));
    }

    public void putFile(World world, String path, byte[] content) throws IOException {
        File target = resolve(world, path);

        try (FileOutputStream fos = new FileOutputStream(target, false)) {
            fos.write(content);
            fos.flush();
        }
    }
}
