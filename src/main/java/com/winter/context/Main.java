package com.winter.context;

import com.winter.context.annotation.Bean;


@Bean
public class Main {
    public static void main(String[] args) throws Exception {
        Context context = Context.getContext("com");
        context.start();
    }
}
