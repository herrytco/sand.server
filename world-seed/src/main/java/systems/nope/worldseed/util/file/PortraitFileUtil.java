package systems.nope.worldseed.util.file;

import org.springframework.stereotype.Service;
import systems.nope.worldseed.model.World;
import systems.nope.worldseed.model.person.Person;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class PortraitFileUtil extends FileUtil {
    public PortraitFileUtil() {
        super("portraits");
    }

    public File resolve(World world, Person person, String path) throws IOException {
        File target = new File(
                String.format("%s/persons/%d/%s",
                        resolveWorldFolder(world).getAbsolutePath(),
                        person.getId(),
                        path)
        );

        File parent = new File(target.getParent());

        if (!parent.exists() && parent.mkdirs())
            logger.info(String.format("Created directory '%s'.", parent.getAbsolutePath()));

        if (!target.exists() && target.createNewFile())
            logger.info(String.format("Created file '%s'.", target.getAbsolutePath()));

        return target;
    }

    public File putFile(World world, Person person, String path, byte[] content) throws IOException {
        File target = resolve(world, person, path);

        try (FileOutputStream fos = new FileOutputStream(target, false)) {
            fos.write(content);
            fos.flush();
        }

        return target;
    }

    private boolean deleteDirectory(File target) {
        File[] allContents = target.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return target.delete();
    }

    public void deleteFolder(World world, Person person, String path) throws IOException {
        File target = resolve(world, person, path);


        if (target.exists() && deleteDirectory(target))
            logger.info(String.format("Deleted file '%s'.", target.getAbsolutePath()));
    }

}
