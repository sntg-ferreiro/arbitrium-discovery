package ar.com.arbitrium.discovery.service

import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.logging.Logger

@Service
class Miner(
    val restTemplate: RestTemplate
) {
    companion object{
        val logger: Logger = Logger.getLogger(this::class.java.simpleName)
    }

    fun mine(listOfTxs: MutableList<Int>): Int {
        listOfTxs.map { t -> logger.info("Transaction: $t") }
        return listOfTxs[0]
    }
}
