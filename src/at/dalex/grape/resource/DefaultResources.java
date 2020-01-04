package at.dalex.grape.resource;

import at.dalex.grape.graphics.ImageUtils;

import java.io.File;

/**
 * This class was written by dalex on 13.09.2017.
 * You are not permitted to edit this file.
 *
 * @author dalex
 */

public class DefaultResources {

    public DefaultResources() {

            /* Resources */
    		new FileContentReader();
//    		FileContentReader.readFile("BaseScript.script");
            Assets.store("resources/maplist.res", "resource.maplist");
            Assets.store(ImageUtils.loadImage(new File("textures/sky/background.png")), "sky.background");
            Assets.store(ImageUtils.loadImage(new File("textures/sky/big_planet.png")), "sky.bigplanet");
            Assets.store(ImageUtils.loadImage(new File("textures/sky/far_planet1.png")), "sky.farplanet.1");
            Assets.store(ImageUtils.loadImage(new File("textures/sky/far_planet2.png")), "sky.farplanet.2");
            Assets.store(ImageUtils.loadImage(new File("textures/sky/ring_planet.png")), "sky.ringplanet");
            Assets.store(ImageUtils.loadImage(new File("textures/sky/stars.png")), "sky.stars");

    }
}
