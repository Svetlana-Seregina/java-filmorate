package ru.yandex.practicum.filmorate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication /*(exclude = {DataSourceAutoConfiguration.class})*/
public class FilmorateApplication {

	public static void main(String[] args) {
		SpringApplication.run(FilmorateApplication.class, args);
	}

}
// @SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})

// Функция автоматической настройки не всегда отвечает задачам приложения.
// В некоторых случаях, например при тестировании, может понадобиться отключить автоконфигурацию подключения
// к базе данных. Чтобы объяснить Spring Boot, что его помощь не требуется, добавьте в аннотацию
// @SpringBootApplication параметр exclude со значением DataSourceAutoConfiguration.class.
//
//@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)