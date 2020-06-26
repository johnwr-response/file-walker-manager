package no.responseweb.imagearchive.filewalkermanager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.io.IOException;

@Slf4j
@SpringBootApplication
@EnableFeignClients
public class FileWalkerManagerApplication {

	public static void main(String[] args) throws IOException, InterruptedException {
		SpringApplication.run(FileWalkerManagerApplication.class, args);
	}

}


