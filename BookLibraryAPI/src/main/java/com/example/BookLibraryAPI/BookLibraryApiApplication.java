package com.example.BookLibraryAPI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookLibraryApiApplication {

	private final static Logger logger = LogManager.getLogger(BookLibraryApiApplication.class);

	public static void main(String[] args) {
		logger.info("Приложение начало работу");
		SpringApplication.run(BookLibraryApiApplication.class, args);
	}

}
