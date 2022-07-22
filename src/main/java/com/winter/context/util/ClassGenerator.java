package com.winter.context.util;

import com.winter.context.annotation.Bean;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.Scanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@Bean
public class ClassGenerator {

    public static final String EXAMPLE = "username:String/password:String/id:int/active:boolean/context:Context";
    public String projectRootPath;
    public String filePath;
    public String packagePath;


    public ClassGenerator(String projectRootPath, String filePath, String packagePath) {
        this.projectRootPath = projectRootPath;
        this.filePath = filePath;
        this.packagePath = packagePath;
    }

    public ClassGenerator() {

    }

    public void generateClassFromXProtocol(String className, String params) throws IOException, ClassNotFoundException {

        File file = new File(filePath + "\\" + className + ".java");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        String imports = "";
        String fields = "";
        String context;

        List<String> p = new ArrayList<>();
        String[] s = params.split("/");
        for (int i = 0; i < s.length; i++) {
            p.add(s[i].split(":")[0]);
            p.add(s[i].split(":")[1]);
        }

        for (int i = 0; i < p.size(); i = i + 2) {
            fields = fields + "\n" + "    private " + p.get(i + 1) + " " + p.get(i) + ";";
            if (!(p.get(i + 1).equals("int") || p.get(i + 1).equals("long") || p.get(i + 1).equals("short") || p.get(i + 1).equals("byte") || p.get(i + 1).equals("float") || p.get(i + 1).equals("double") || p.get(i + 1).equals("boolean") || p.get(i + 1).equals("char") || p.get(i + 1).equals("String"))) {
                Reflections reflections = new Reflections(projectRootPath,new SubTypesScanner(false));
                Set<Class<?>> classes = reflections.getSubTypesOf(Object.class);
                for (Class c:
                     classes) {
                    if (c.getSimpleName().equals(p.get(i + 1))) {
                        imports = imports + "\n" + "import " + c.getPackageName() + "." + c.getSimpleName() + ";";
                    }
                }

            }

        }
        context = "package " + packagePath + ";" + "\n\n\n" +
                "import lombok.Data;" + "\n" +
                imports +
                "\n\n" + "@Data" + "\n" +
                "public class " + className + " {" +
                "\n" + fields + "\n\n" +
                "}";

        byte[] b = context.getBytes();
        fileOutputStream.write(b);
        fileOutputStream.close();

    }
}

