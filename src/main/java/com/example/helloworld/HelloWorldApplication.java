package com.example.helloworld;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@SpringBootApplication
public class HelloWorldApplication {

	public static void main(String[] args) {
		loadEnv();
		SpringApplication.run(HelloWorldApplication.class, args);
		System.out.println("helloe worlddddd");
	}

	private static void loadEnv() {
		try {
			Path envPath = Paths.get(".env");
			if (Files.exists(envPath)) {
				List<String> lines = Files.readAllLines(envPath);
				for (String line : lines) {
					line = line.trim();
					if (!line.isEmpty() && !line.startsWith("#") && line.contains("=")) {
						int idx = line.indexOf("=");
						String key = line.substring(0, idx).trim();
						String value = line.substring(idx + 1).trim();
						if ((value.startsWith("\"") && value.endsWith("\"")) ||
						    (value.startsWith("'") && value.endsWith("'"))) {
							value = value.substring(1, value.length() - 1);
						}
						if (System.getProperty(key) == null && System.getenv(key) == null) {
							System.setProperty(key, value);
						}
					}
				}
			}
		} catch (Exception e) {
			System.err.println("Notice: Could not load .env file: " + e.getMessage());
		}
	}

}
