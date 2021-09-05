package ar.com.arbitrium.discovery

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate
import java.time.Duration

@SpringBootApplication
class DiscoveryApplication



fun main(args: Array<String>) {
	runApplication<DiscoveryApplication>(*args)
}
