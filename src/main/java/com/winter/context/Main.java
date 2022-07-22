package com.winter.context;

import com.winter.context.annotation.Bean;
import com.winter.context.util.ClassGenerator;


@Bean
public class Main {

    public static void main(String[] args) throws Exception {
        Context context = new Context("com");
        context.start();


        String packagePath = "com.winter.context.generatedClasses";
        String projectRootPath = "com.winter.context";
        String example = "username:String/password:String/id:int/active:boolean/context:Context/v:Integer/c:ClassGenerator/ss:Scanner";
        String className = "User";

        ClassGenerator classGenerator = new ClassGenerator(projectRootPath, packagePath);
        classGenerator.generateClassFromXProtocol(className, example);

    }
}
