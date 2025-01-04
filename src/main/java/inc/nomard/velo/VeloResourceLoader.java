package inc.nomard.velo;

import java.net.URL;

public class VeloResourceLoader {
    public static URL loadResourceFile(String path) {
        return VeloResourceLoader.class.getResource(path);
    }
}
