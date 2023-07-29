package net.vladislemon.mmcpu.bootstrap;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length != 4) {
            System.out.println("Usage: <cmd> <java path> <instance path> <minecraft path> <url to modpack>");
            System.exit(1);
        }
        Path javaPath = Paths.get(args[0]);
        System.out.println("Java path: " + javaPath);
        Path instancePath = Paths.get(args[1]);
        System.out.println("Instance path: " + instancePath);
        Path minecraftPath = Paths.get(args[2]);
        System.out.println("Minecraft path: " + minecraftPath);
        URI modpackUri = asDirectory(args[3]);
        System.out.println("Modpack URI: " + modpackUri);
        startPackwiz(javaPath, minecraftPath, modpackUri);
        startMMCPackUpdater(javaPath, minecraftPath, instancePath, modpackUri);
    }

    private static void startPackwiz(Path javaPath, Path minecraftPath, URI modpackUri) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(
                javaPath.toString(),
                "-jar",
                minecraftPath.resolve("packwiz-installer-bootstrap.jar").toString(),
                modpackUri.resolve("pack.toml").toString()
        );
        processBuilder.directory(minecraftPath.toFile());
        processBuilder.inheritIO();
        Process process = processBuilder.start();
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Packwiz exited with code " + exitCode);
        }
    }

    private static void startMMCPackUpdater(Path javaPath, Path minecraftPath, Path instancePath, URI modpackUri) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(
                javaPath.toString(),
                "-jar",
                minecraftPath.resolve("mmc-pack-updater.jar").toString(),
                instancePath.toString(),
                modpackUri.toString()
        );
        processBuilder.inheritIO();
        Process process = processBuilder.start();
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("mmc-pack-updater exited with code " + exitCode);
        }
    }

    private static URI asDirectory(String uri) {
        return !uri.endsWith("/") ? URI.create(uri.concat("/")) : URI.create(uri);
    }
}
