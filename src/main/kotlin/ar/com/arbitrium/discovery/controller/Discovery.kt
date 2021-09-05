package ar.com.arbitrium.discovery.controller

import ar.com.arbitrium.discovery.model.Node
import ar.com.arbitrium.discovery.model.NodeHealthError
import ar.com.arbitrium.discovery.repository.NodeHealthErrorRepository
import ar.com.arbitrium.discovery.repository.NodeRepository
import ar.com.arbitrium.discovery.service.HealthChecker
import org.springframework.web.bind.annotation.*
import java.util.logging.Logger
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

@RequestMapping
@RestController
class Discovery(var checker: HealthChecker, var nodeRepository: NodeRepository, var errorRepository: NodeHealthErrorRepository) {
    companion object{
        val logger = Logger.getLogger(this::class.java.simpleName)
    }

    @GetMapping
    fun findNodes(): MutableIterable<Node> = nodeRepository.findAll()

    @PostMapping()
    fun addNewNode(@RequestBody node: Node) {
        nodeRepository.save(node)
    }

    @GetMapping("/health")
    fun doCheck() = checker.doGralHealthCheck()

    @GetMapping("/health-error")
    fun findErrors(): MutableIterable<NodeHealthError> = errorRepository.findAll()
}