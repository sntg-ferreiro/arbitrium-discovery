package ar.com.arbitrium.discovery.service

import ar.com.arbitrium.discovery.model.Node
import ar.com.arbitrium.discovery.model.NodeHealthError
import ar.com.arbitrium.discovery.repository.NodeHealthErrorRepository
import ar.com.arbitrium.discovery.repository.NodeRepository
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import java.net.ConnectException
import java.time.Instant
import java.util.*
import java.util.logging.Logger

@Service
class HealthChecker(
    var restTemplate: RestTemplate,
    var errorRepository: NodeHealthErrorRepository,
    var nodeRepository: NodeRepository
) {
    companion object{
        val logger = Logger.getLogger(this::class.java.simpleName)
    }


    fun healthCheck(node: Node){
        logger.info("Cheking health on node: $node")
        try{
            var response = restTemplate.getForEntity("${node.dominio}${node.actuator}", LinkedHashMap::class.java)
            logger.info("$node is OK!")
        } catch (e: HttpClientErrorException){
            logger.warning("$node is NOT OK!")
            errorRepository.save(NodeHealthError(node.id, Instant.now().toString(), e.rawStatusCode))
        } catch (e: ConnectException){
            logger.warning("$node is NOT OK!")
            errorRepository.save(NodeHealthError(node.id, Instant.now().toString(), 500))
        }
    }

    fun doGralHealthCheck(){
        var nodes = nodeRepository.findAll()
        nodes.map {n ->  healthCheck(n) }
    }
}