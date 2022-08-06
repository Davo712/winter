package com.winter.context;

import com.winter.context.annotation.Autowired;
import com.winter.context.annotation.Bean;
import com.winter.context.generatedClasses.User;
import com.winter.context.service.BeanCreatorService;
import com.winter.context.util.AnnotationService;
import com.winter.context.util.ClassGenerator;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;


@Bean
public class Main {

    @Autowired
    private static ClassGenerator classGenerator;

    public static void main(String[] args) throws Exception {
        Context context = new Context("com");
        context.start();

        System.out.println(AnnotationService.getAnnotatedClasses(Bean.class, "com"));
        System.out.println(classGenerator.toString());

        String packagePath = "com.winter.context.generatedClasses";
        String projectRootPath = "com.winter.context";
        String example = "username:String/password:String/id:int/active:boolean/context:Context/c:ClassGenerator/r:Reflections/f:File/dsds:Object/ll:Boolean";
        String className = "User";
        List<String> imports = new ArrayList<>();
        imports.add("org.reflections.Reflections");
        Class superClass = ClassGenerator.class;
        List<Class> impl = new ArrayList<>();
        impl.add(Array.class);
        impl.add(IntStream.class);


      //  ClassGenerator classGenerator = new ClassGenerator(projectRootPath, packagePath, imports);
      // classGenerator.generateClassFromXProtocol(className, example);


        List<String> l = new ArrayList<>();
        l.add("username:String/password:String/id:int/active:boolean/context:Context/c:ClassGenerator/r:Reflections/f:File/dsds:Object/ll:Boolean");
        l.add("username:String/password:String/id:int/f:File");
        List<String> names = new ArrayList<>();
        names.add("User");
        names.add("TestUser");


      //  classGenerator.generateMoreClasses(names, l);



    }
}
