package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.List;

@SpringBootApplication
@RestController
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format("Hello %s!", name);
	}

	@GetMapping("/getImage")
	public String getImage() {
		ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create();
		Region region = Region.US_EAST_1;
		S3Client s3 = S3Client.builder()
				.region(region)
				.credentialsProvider(credentialsProvider)
				.build();
		StringBuilder returnStatement = new StringBuilder();
		try {
			ListObjectsResponse response = s3.listObjects(ListObjectsRequest.builder().bucket("rmerugu-assignment1").build());
			List<S3Object> objects = response.contents();
			for (S3Object myValue : objects) {
				returnStatement.append("The name of the key is " + myValue.key() + "\n");
				returnStatement.append("The object is " + calKb(myValue.size()) + " KBs\n");
				returnStatement.append("The owner is " + myValue.owner());
			}

		} catch (S3Exception e) {
			System.err.println(e.awsErrorDetails().errorMessage());
			return "Unable to list objects";
		}
		return returnStatement.toString();
	}


	private static long calKb(Long val) {
		return val/1024;
	}
}