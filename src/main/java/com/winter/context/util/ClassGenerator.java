package com.winter.context.util;

import com.winter.context.annotation.Bean;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Bean
public class ClassGenerator {

    public static final String XPROTOCOLEXAMPLE = "username:String/password:String/id:int/active:boolean/context:Context";
    public static final String IMPORTEXAMPLE = "org.reflections.Reflections";

    public String projectRootPath;
    public String packagePath;
    public List<String> otherClassesImports;

    public ClassGenerator(String projectRootPath, String packagePath, List<String> otherClassesImports) {
        this.projectRootPath = projectRootPath;
        this.packagePath = packagePath;
        this.otherClassesImports = otherClassesImports;
    }

    public ClassGenerator(String projectRootPath, String packagePath) {
        this.projectRootPath = projectRootPath;
        this.packagePath = packagePath;
    }

    public ClassGenerator() {

    }

    public void generateMoreClasses(List<String> classNames, List<String> params) throws IOException, ClassNotFoundException {
        if (classNames.size() == params.size()) {
            ClassGenerator c = new ClassGenerator(projectRootPath,packagePath,otherClassesImports);
            for (int i = 0; i < classNames.size(); i++) {
                c.generateClassFromXProtocol(classNames.get(i),params.get(i));
            }
        }
    }

    public void generateClassFromXProtocol(String className, String params) throws IOException, ClassNotFoundException {
        String filePath = "src/main/java/" + packagePath.replace(".", "/");

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

        Reflections reflections = new Reflections(projectRootPath, new SubTypesScanner(false));
        Set<Class<?>> classes = reflections.getSubTypesOf(Object.class);
        Set<String> classNames = new HashSet<>();
        for (Class c :
                classes) {
            classNames.add(c.getSimpleName());
        }

        boolean isOtherClasses = false;
        for (int i = 0; i < p.size(); i = i + 2) {
            if (!(p.get(i + 1).equals("int") || p.get(i + 1).equals("long") || p.get(i + 1).equals("short") || p.get(i + 1).equals("byte") || p.get(i + 1).equals("float") || p.get(i + 1).equals("double") || p.get(i + 1).equals("boolean") || p.get(i + 1).equals("char") || p.get(i + 1).equals("String")) || p.get(i + 1).equals("Object") || p.get(i + 1).equals("Integer") || p.get(i + 1).equals("Long") || p.get(i + 1).equals("Short") || p.get(i + 1).equals("Float") || p.get(i + 1).equals("Double") || p.get(i + 1).equals("Byte") || p.get(i + 1).equals("Boolean")) {
                if (!(classNames.contains(p.get(i + 1)))) {
                    isOtherClasses = true;
                }
                for (Class c :
                        classes) {
                    if (c.getSimpleName().equals(p.get(i + 1))) {
                        imports = imports + "\n" + "import " + c.getPackageName() + "." + c.getSimpleName() + ";";
                    }
                }
            }
            fields = fields + "\n" + "    private " + p.get(i + 1) + " " + p.get(i) + ";";
        }
        if (isOtherClasses) {
            imports = imports + "\n" + "import " + "java.util.*" + ";";
            imports = imports + "\n" + "import " + "java.io.*" + ";";
        }
        if (otherClassesImports != null) {
            if ((!otherClassesImports.isEmpty())&&isOtherClasses) {
                for (String impr :
                        otherClassesImports) {
                    imports = imports + "\n" + "import " + impr + ";";
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
        System.out.println("Generated class: " + className + ".java");
        fileOutputStream.close();
    }
}

