package systems.nope.worldseed.util.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import systems.nope.worldseed.model.World;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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

    public File resolve(World world) {
        File target = new File(String.format("%s/%d", root().getAbsolutePath(), world.getId()));

        if (!target.exists() && target.mkdirs())
            logger.info(String.format("Created directory '%s'", target.getAbsolutePath()));

        return target;
    }

    public File resolve(World world, String path) throws IOException {
        File target = new File(String.format("%s/%s", resolve(world).getAbsolutePath(), path));

        if (target.createNewFile())
            logger.info(String.format("Created file '%s'.", target.getAbsolutePath()));

        return target;
    }

    public void putFile(World world, String path, byte[] content) throws IOException {
        File target = resolve(world, path);

        try (FileOutputStream fos = new FileOutputStream(target, false)) {
            fos.write(content);
            fos.flush();
        }
    }
}
