package ar.com.arbitrium.discovery.controller

import ar.com.arbitrium.discovery.model.Transaccion
import ar.com.arbitrium.discovery.service.Miner
import org.springframework.web.bind.annotation.*

@RequestMapping
@RestController
class Discovery(var miner: Miner) {

    @GetMapping("/salud")
    fun doCheck() = miner.checkSalud()

    @PostMapping("/transaction")
    fun mine(@RequestBody txs: MutableList<Transaccion>) = miner.mine(txs)

    @GetMapping("/resultados/org/{idOrg}/decision/{idDec}")
    fun mine(@PathVariable("idOrg") idOrg: Long, @PathVariable("idDec") idDec: Long ) = miner.buscarResultadosDecision(idOrg, idDec)
}