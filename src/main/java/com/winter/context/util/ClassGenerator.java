package com.winter.context.util;

import com.sun.codemodel.JCodeModel;
import com.winter.context.annotation.Bean;
import org.codehaus.plexus.component.configurator.ComponentConfigurationException;
import org.jsonschema2pojo.*;
import org.jsonschema2pojo.rules.RuleFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Bean
public class ClassGenerator {

    public static final String EXAMPLE = "username:string/password:string/id:int/active:boolean";

    public void generateClassFromJson(String filePath, String className, String json) throws IOException, ComponentConfigurationException {
        JCodeModel jcodeModel = new JCodeModel();
        GenerationConfig config = new DefaultGenerationConfig() {
            @Override
            public boolean isGenerateBuilders() {
                return true;
            }

            @Override
            public SourceType getSourceType() {
                return SourceType.JSON;
            }

        };
        File outputJavaClassDirectory = new File(filePath);
        SchemaMapper mapper = new SchemaMapper(new RuleFactory(config, new Jackson2Annotator(config), new SchemaStore()), new SchemaGenerator());
        mapper.generate(jcodeModel, className, "", json);
        jcodeModel.build(outputJavaClassDirectory);
    }

    public void generateClassFromXProtocol(String generationClassPath, String className, String context) throws IOException, ComponentConfigurationException {
        List<String> params = new ArrayList<>();
        String[] strings = context.split("/");
        for (int i = 0; i < strings.length; i++) {
            params.add("\"" + strings[i].split(":")[0] + "\"");
            params.add("\"" + strings[i].split(":")[1] + "\"");
        }
        String j = "";
        String json = "";
        for (int i = 0; i < params.size(); i = i + 2) {
            if (i == params.size() - 2) {
                switch (params.get(i + 1)) {
                    case "\"int\"":
                        j = j + " " + params.get(i) + ":" + " " + "1" + " ";
                        break;
                    case "\"boolean\"":
                        j = j + " " + params.get(i) + ":" + " " + "true" + " ";
                        break;
                    case "\"string\"":
                        j = j + " " + params.get(i) + ":" + " " + params.get(i + 1) + " ";
                        break;
                    case "\"object\"":
                        j = j + " " + params.get(i) + ":" + " " + "null" + " ";
                        break;
                }
            } else {
                switch (params.get(i + 1)) {
                    case "\"int\"":
                        j = j + " " + params.get(i) + ":" + " " + "1" + ",";
                        break;
                    case "\"boolean\"":
                        j = j + " " + params.get(i) + ":" + " " + "true" + ",";
                        break;
                    case "\"string\"":
                        j = j + " " + params.get(i) + ":" + " " + params.get(i + 1) + ",";
                        break;
                    case "\"object\"":
                        j = j + " " + params.get(i) + ":" + " " + "null" + " ";
                        break;
                }
            }

        }

        json = "{" + j + "}";
        System.out.println(json);
        generateClassFromJson(generationClassPath, className, json);
    }

    public void generateClassFromString(String generationClassPath, String className, String params) throws IOException {
        File file = new File(generationClassPath);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        String context = "package com.winter.context.generatedClasses; \n" +
                "public class " + className + " {\n" +
                "}";
        byte[] b = context.getBytes();
        fileOutputStream.write(b);


    }


}

