package com.winter.context;

import com.sun.codemodel.JCodeModel;
import com.winter.context.annotation.Autowired;
import com.winter.context.annotation.Bean;
import com.winter.context.service.BeanCreatorService;
import com.winter.context.util.ClassGenerator;
import org.jsonschema2pojo.*;
import org.jsonschema2pojo.rules.RuleFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

@Bean
public class Main {

    @Autowired
    public static ClassGenerator classGenerator;

    public static void main(String[] args) throws Exception {
        Context context = new Context("com");
        context.start();
        System.out.println(BeanCreatorService.beans.containsKey("main"));

        String filePath = "C:\\Users\\Davit.gevorgyan\\IdeaProjects\\winter\\src\\main\\java\\com\\winter\\context\\generatedClasses";
        String className = "Userrr";
        String json = "{\n" +
                "        \"javaType\" : \"Context\",\n" +
                "        \"type\" : \"object\"\n" +
                "    }";
//        classGenerator.generateClassFromJson(filePath,className,json);
//
//        String x = "username:string/password:string/id:int/name:string/surname:string/active:boolean/object:object";
//        classGenerator.generateClassFromXProtocol(filePath, className, x);

        File file = new File("C:\\Users\\Davit.gevorgyan\\IdeaProjects\\winter\\src\\main\\java\\com\\winter\\context\\generatedClasses\\" + className + ".java");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(12211);

    }
}
