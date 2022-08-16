package com.winter.context.classGenerator;

import com.winter.context.Context;
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
    public Class superClass;
    public List<Class> implInterfaces;

    public ClassGenerator(String projectRootPath, String packagePath, List<String> otherClassesImports, Class superClass, List<Class> implInterfaces) {
        this.projectRootPath = projectRootPath;
        this.packagePath = packagePath;
        this.otherClassesImports = otherClassesImports;
        this.superClass = superClass;
        this.implInterfaces = implInterfaces;
    }

    public ClassGenerator(String projectRootPath, String packagePath, List<String> otherClassesImports, List<Class> implInterfaces) {
        this.projectRootPath = projectRootPath;
        this.packagePath = packagePath;
        this.otherClassesImports = otherClassesImports;
        this.implInterfaces = implInterfaces;
    }

    public ClassGenerator(String projectRootPath, String packagePath, List<String> otherClassesImports, Class superClass) {
        this.projectRootPath = projectRootPath;
        this.packagePath = packagePath;
        this.otherClassesImports = otherClassesImports;
        this.superClass = superClass;
    }

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
        if (!Context.isRunned) {
            System.out.println("Winter is not runned");
            return;
        }

        if (classNames.size() == params.size()) {
            ClassGenerator c = new ClassGenerator(projectRootPath, packagePath, otherClassesImports, superClass, implInterfaces);
            for (int i = 0; i < classNames.size(); i++) {
                c.generateClassFromXProtocol(classNames.get(i), params.get(i));
            }
        }
    }

    public void generateClassFromXProtocol(String className, String params) throws IOException, ClassNotFoundException {
        if (!Context.isRunned) {
            System.out.println("Winter is not runned");
            return;
        }

        String filePath = "src/main/java/" + packagePath.replace(".", "/");
        File file = new File(filePath + "\\" + className + ".java");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        String imports = "";
        String fields = "";
        String content;

        // Get params from xProtocol
        List<String> typesAndObjectsNames = new ArrayList<>();
        String[] s = params.split("/");
        for (int i = 0; i < s.length; i++) {
            typesAndObjectsNames.add(s[i].split(":")[0]);
            typesAndObjectsNames.add(s[i].split(":")[1]);
        }

        // Scan all classes in project and add class names in Set
        Reflections reflections = new Reflections(projectRootPath, new SubTypesScanner(false));
        Set<Class<?>> classes = reflections.getSubTypesOf(Object.class);
        Set<String> classNames = new HashSet<>();
        for (Class c :
                classes) {
            classNames.add(c.getSimpleName());
        }

        // Get fields, imports, extends, implements
        boolean isOtherClasses = false;
        for (int i = 0; i < typesAndObjectsNames.size(); i = i + 2) {
            if (!(typesAndObjectsNames.get(i + 1).equals("int") || typesAndObjectsNames.get(i + 1).equals("long") || typesAndObjectsNames.get(i + 1).equals("short") || typesAndObjectsNames.get(i + 1).equals("byte") || typesAndObjectsNames.get(i + 1).equals("float") || typesAndObjectsNames.get(i + 1).equals("double") || typesAndObjectsNames.get(i + 1).equals("boolean") || typesAndObjectsNames.get(i + 1).equals("char") || typesAndObjectsNames.get(i + 1).equals("String")) || typesAndObjectsNames.get(i + 1).equals("Object") || typesAndObjectsNames.get(i + 1).equals("Integer") || typesAndObjectsNames.get(i + 1).equals("Long") || typesAndObjectsNames.get(i + 1).equals("Short") || typesAndObjectsNames.get(i + 1).equals("Float") || typesAndObjectsNames.get(i + 1).equals("Double") || typesAndObjectsNames.get(i + 1).equals("Byte") || typesAndObjectsNames.get(i + 1).equals("Boolean")) {
                if (!(classNames.contains(typesAndObjectsNames.get(i + 1)))) {
                    isOtherClasses = true;
                }
                for (Class c :
                        classes) {
                    if (c.getSimpleName().equals(typesAndObjectsNames.get(i + 1))) {
                        imports = imports + "\n" + "import " + c.getPackageName() + "." + c.getSimpleName() + ";";
                    }
                }
            }
            fields = fields + "\n" + "    private " + typesAndObjectsNames.get(i + 1) + " " + typesAndObjectsNames.get(i) + ";";
        }
        if (isOtherClasses) {
            imports = imports + "\n" + "import " + "java.util.*" + ";";
            imports = imports + "\n" + "import " + "java.io.*" + ";";
        }
        if (otherClassesImports != null) {
            if ((!otherClassesImports.isEmpty()) && isOtherClasses) {
                for (String impr :
                        otherClassesImports) {
                    imports = imports + "\n" + "import " + impr + ";";
                }
            }
        }
        String extendContent = "";
        if (superClass != null) {
            imports = imports + "\n" + "import " + superClass.getCanonicalName() + ";";
            extendContent = " extends " + superClass.getSimpleName();
        }
        String implContent = "";
        if (implInterfaces != null) {
            implContent = " implements";
            for (int i = 0; i < implInterfaces.size(); i++) {
                if (implInterfaces.size() - i == 1) {
                    imports = imports + "\n" + "import " + implInterfaces.get(i).getCanonicalName() + ";";
                    implContent = implContent + " " + implInterfaces.get(i).getSimpleName();
                    break;
                }
                imports = imports + "\n" + "import " + implInterfaces.get(i).getCanonicalName() + ";";
                implContent = implContent + " " + implInterfaces.get(i).getSimpleName() + ",";
            }
        }

        // Get content and generate class
        content = "package " + packagePath + ";" + "\n\n\n" +
                "import lombok.Data;" + "\n" +
                imports +
                "\n\n" + "@Data" + "\n" +
                "public class " + className + extendContent + implContent + " {" +
                "\n" + fields + "\n\n" +
                "}";

        byte[] b = content.getBytes();
        fileOutputStream.write(b);
        System.out.println("Generated class: " + className + ".java");
        fileOutputStream.close();
    }
}

