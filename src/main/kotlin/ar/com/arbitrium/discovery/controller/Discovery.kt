package ar.com.arbitrium.discovery.controller

import ar.com.arbitrium.discovery.model.Transaccion
import ar.com.arbitrium.discovery.service.Miner
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.context.request.WebRequest

@RequestMapping
@RestController
class Discovery(var miner: Miner) {

    @GetMapping("/salud")
    fun doCheck() = miner.checkSalud()

    @PostMapping("/transaction")
    fun mine(@RequestBody txs: MutableList<Transaccion>) = miner.mine(txs)

    @GetMapping("/resultados/org/{idOrg}/decision/{idDec}")
    fun resultados(@PathVariable("idOrg") idOrg: Long, @PathVariable("idDec") idDec: Long ) = miner.buscarResultadosDecision(idOrg, idDec)

    @ExceptionHandler(value = [(RuntimeException::class)])
    fun handleException(ex: RuntimeException, request: WebRequest): Any {
        return ResponseEntity.status(500).body(object {
            val error = ex.localizedMessage
        })
    }
}