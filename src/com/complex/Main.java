package com.complex;

import java.io.File;
import java.net.URISyntaxException;

public class Main {

    public static void main(String[] args) throws URISyntaxException {
        String editorPath = new File(Main.class.getProtectionDomain()
                .getCodeSource().getLocation().toURI()).toString();

        editorPath = System.getProperty("os.name").toLowerCase().contains("windows") ? editorPath.replaceAll("\\\\", "/") : editorPath;

        int lastSeparatorIndex = editorPath.lastIndexOf('/');
        File editor_executable_directory = new File(editorPath.substring(0, lastSeparatorIndex));


        ComplexGame gameInstance = new ComplexGame(editor_executable_directory.getAbsolutePath());
        gameInstance.startEngine();
    }
}
