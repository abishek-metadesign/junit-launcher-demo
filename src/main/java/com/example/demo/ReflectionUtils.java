package com.example.demo;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;

public class ReflectionUtils {

    public static Optional<ClassLoader> createCustomClassLoader(Set<Path> entries){
        if(!entries.isEmpty()){
            URL[] urls = entries.stream().map(ReflectionUtils::toURL).toArray(URL[]::new);
            ClassLoader customClassLoader = URLClassLoader.newInstance( urls);
            return Optional.of(customClassLoader);
        }
        return Optional.empty();
    }


    public static Optional<ClassLoader> createCustomClassLoader(Set<Path> entries,ClassLoader parent){
        if(!entries.isEmpty()){
            URL[] urls = entries.stream().map(ReflectionUtils::toURL).toArray(URL[]::new);
            ClassLoader customClassLoader = URLClassLoader.newInstance( urls,parent);
            return Optional.of(customClassLoader);
        }
        return Optional.empty();
    }



    private static URL toURL(Path path) {
        try {
            return path.toUri().toURL();
        }
        catch (Exception ex) {
            throw new RuntimeException("Invalid classpath entry: " + path, ex);
        }
    }

}
