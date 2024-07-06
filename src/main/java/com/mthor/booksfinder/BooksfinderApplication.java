package com.mthor.booksfinder;

import com.mthor.booksfinder.principal.MainControl;
import com.mthor.booksfinder.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BooksfinderApplication implements CommandLineRunner {

	private final DataService dataService;

	@Autowired
	public BooksfinderApplication(DataService dataService) {
		this.dataService = dataService;
	}

	public static void main(String[] args) {
		SpringApplication.run(BooksfinderApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		MainControl mainControl = new MainControl(dataService);
		mainControl.menu();
	}
}
