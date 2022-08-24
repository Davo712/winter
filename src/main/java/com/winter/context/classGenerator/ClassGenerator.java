package com.winter.context.classGenerator;

import com.winter.context.Context;
import com.winter.context.service.BeanCreatorService;
import lombok.Data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class ClassGenerator {

    private Class superClass;
    private Class implClass;
    private String packagePath;
    private String className;
    private String fields = "";
    private String imports = "";
    private String methods = "";

    public ClassGenerator(String packagePath) throws Exception {
        this.packagePath = packagePath;
    }

    public void addField(Class type, String name) {
        fields = fields + "\n" + "    " + type.getSimpleName() + " " + name + ";";
    }

    public void addField(Class type, String name, String modifier) {
        fields = fields + "\n" + "    " + modifier + " " + type.getSimpleName() + " " + name + ";";
    }

    public void addField(Class type, String name, boolean isStatic) {
        if (isStatic) {
            fields = fields + "\n" + "    " + "static" + " " + type.getSimpleName() + " " + name + ";";
        } else {
            fields = fields + "\n" + "    " + type.getSimpleName() + " " + name + ";";
        }
    }

    public void addField(Class type, String name, boolean isStatic, String modifier) {
        if (isStatic) {
            fields = fields + "\n" + "    " + modifier + " " + "static" + " " + type.getSimpleName() + " " + name + ";";
        } else {
            fields = fields + "\n" + "    " + modifier + " " + type.getSimpleName() + " " + name + ";";
        }
    }


    public void addImport(String package_) {
        imports = imports + "\n" + "import" + " " + package_ + ";";
    }

    public void addMethod(String modifier, Boolean isStatic, Class returnType, String methodName, String parameters, String body) throws NoSuchMethodException {
        String static_ = "";
        if (isStatic) {
            static_ = "static";
        }
        methods = methods + "\n" + "    " + modifier + " " + static_ + " " + returnType.getSimpleName() + " " + methodName + "(" + parameters + ")" + " {" + "\n"
                + "        " + body
                + "\n"
                + "    }";
    }


    public void generate() throws IOException {
        if (!Context.isRunned) {
            System.out.println("Winter is not runned");
            return;
        }


        String filePath = "src/main/java/" + packagePath.replace(".", "/");
        File file = new File(filePath + "\\" + className + ".java");
        FileOutputStream fileOutputStream = new FileOutputStream(file);

        String extend = "";
        if (superClass != null) {
            extend = " extends " + superClass.getSimpleName();
        }
        String impl = "";
        if (implClass != null) {
            impl = " implements " + implClass.getSimpleName();
        }
        String content = "package " + packagePath + ";" + "\n\n" +
                "import lombok.Data;" + "\n" +
                imports + "\n" +
                "\n" + "@Data" + "\n" +
                "public class " + className + extend + impl + " {" +
                "\n" + fields + "\n" +
                "\n" + methods + "\n" +
                "}";
        fileOutputStream.write(content.getBytes());
        System.out.println(className + ".java" + " generated");
    }
}
