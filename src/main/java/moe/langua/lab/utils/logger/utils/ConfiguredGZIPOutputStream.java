package moe.langua.lab.utils.logger.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

public class ConfiguredGZIPOutputStream extends GZIPOutputStream {
    public ConfiguredGZIPOutputStream(OutputStream out) throws IOException {
        super(out);
        def.setLevel(9);
    }

}
