package ar.com.arbitrium.discovery.controller

import ar.com.arbitrium.discovery.model.Node
import ar.com.arbitrium.discovery.model.NodeHealthError
import ar.com.arbitrium.discovery.model.Transaccion
import ar.com.arbitrium.discovery.repository.NodeHealthErrorRepository
import ar.com.arbitrium.discovery.repository.NodeRepository
import ar.com.arbitrium.discovery.service.HealthChecker
import ar.com.arbitrium.discovery.service.Miner
import ar.com.arbitrium.discovery.service.PeerService
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.support.AbstractMultipartHttpServletRequest
import java.util.logging.Logger
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

@RequestMapping
@RestController
class Discovery(var checker: HealthChecker,
                var nodeRepository: NodeRepository,
                var errorRepository: NodeHealthErrorRepository,
                var miner: Miner,
                var peers: PeerService
) {

    @GetMapping
    fun findNodes(): MutableIterable<Node> = nodeRepository.findAll()

    @PostMapping()
    fun addNewNode(@RequestBody node: Node) = nodeRepository.save(node)

    @GetMapping("/nuevo-peer")
    fun newPeer() = peers.nuevo()

    @GetMapping("/health")
    fun doCheck() = checker.doGralHealthCheck()

    @GetMapping("/health-error")
    fun findErrors(): MutableIterable<NodeHealthError> = errorRepository.findAll()

    @PostMapping("/transaction")
    fun mine(@RequestBody txs: MutableList<Transaccion>) = miner.mine(txs)

    @GetMapping("/resultados/org/{idOrg}/decision/{idDec}")
    fun mine(@PathVariable("idOrg") idOrg: Long, @PathVariable("idDec") idDec: Long ) = miner.buscarResultadosDecision(idOrg, idDec)
}