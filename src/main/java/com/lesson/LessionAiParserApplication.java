package com.lesson;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan("com.lesson.mapper")
@SpringBootApplication
public class LessionAiParserApplication {

	public static void main(String[] args) {
		SpringApplication.run(LessionAiParserApplication.class, args);
	}

}
