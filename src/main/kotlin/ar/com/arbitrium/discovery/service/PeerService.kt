package ar.com.arbitrium.discovery.service

import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class PeerService(
    var restTemplate: RestTemplate
) {
    fun nuevo(): Unit {

    }

}
