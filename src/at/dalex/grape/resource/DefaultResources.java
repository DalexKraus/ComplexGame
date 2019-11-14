package at.dalex.grape.resource;

import java.io.IOException;

/**
 * This class was written by dalex on 13.09.2017.
 * You are not permitted to edit this file.
 *
 * @author dalex
 */

public class DefaultResources {

    public DefaultResources() {

//    	try {
            /* Resources */
    		new FileContentReader();
//    		FileContentReader.readFile("BaseScript.script");
            Assets.store("resources/maplist.res", "resource.maplist");

//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
    }
}
