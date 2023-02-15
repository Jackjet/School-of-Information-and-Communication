package d1.project.caict.business;


import d1.project.caict.business.utils.Constants;

import java.io.File;

/**
 * @author maorui
 */
public class Init {

    public static void init() {
        new File(Constants.ACTIVITIES).mkdirs();
        new File(Constants.USERS).mkdirs();
    }
}
