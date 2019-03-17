package org.czc.cmq.broker;

import com.czc.cmq.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by chenzhichao on 19/3/17.
 */
public class Config {

    private static Logger logger = LoggerFactory.getLogger(Config.class);
    public static volatile String homeDir;
    public static final String DEFAULT_DIR = "./";
    public void loadConfig() throws IOException {
        Properties properties = new Properties();
        InputStream inputStream = Main.class.getResourceAsStream("/broker.properties");
        properties.load(inputStream);
        String homeDir = properties.getProperty("home");
        if (StringUtil.isEmpty(homeDir)) {
            logger.warn("could not find homeDir!");
            Config.homeDir = DEFAULT_DIR;
        } else {
            Config.homeDir = homeDir;
        }
        File file = new File(homeDir);
        if (file.isFile()) {
            logger.error("homedir have bean named file !");
            System.exit(-1);
        }
    }
}
