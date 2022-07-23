package com.winter.context;

import com.winter.context.annotation.Bean;
import com.winter.context.util.ClassGenerator;

import java.util.ArrayList;
import java.util.List;


@Bean
public class Main {

    public static void main(String[] args) throws Exception {
        Context context = new Context("com");
        context.start();


        String packagePath = "com.winter.context.generatedClasses";
        String projectRootPath = "com.winter.context";
        String example = "username:String/password:String/id:int/active:boolean/context:Context/c:ClassGenerator/r:Reflections/f:File";
        String className = "User";
        List<String> imports = new ArrayList<>();
        imports.add("org.reflections.Reflections");
        imports.add("java.io.File");

        ClassGenerator classGenerator = new ClassGenerator(projectRootPath, packagePath, imports);
        classGenerator.generateClassFromXProtocol(className, example);

    }
}
