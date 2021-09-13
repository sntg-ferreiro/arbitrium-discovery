package ar.com.arbitrium.discovery.service

import ar.com.arbitrium.discovery.model.Transaccion
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity
import org.springframework.web.client.postForEntity
import java.util.logging.Logger
import kotlin.random.Random

@Service
class Miner(
    val restTemplate: RestTemplate,
    var transacciones: MutableList<Transaccion>,
    @Value("\${limite}", )val limite_transac: Int,
    @Value("#{'\${pares}'.split(',')}") val pares: MutableList<String>
) {

    companion object{
        val logger: Logger = Logger.getLogger(this::class.java.simpleName)
    }

    fun mine(txs: MutableList<Transaccion>): MutableList<Transaccion> {
        logger.info("Agregando txs a transacciones... ")
        agregarTransacciones(txs)
        if(transacciones.size >= limite_transac) llamarMineroRandom()
        logger.info("Ahora tenemos: ${transacciones.size} transacciones!")
        return transacciones
    }

    private fun agregarTransacciones(txs: MutableList<Transaccion>) {
        //TODO: LOGICA DE SI LLEGA UNA TRANSACCION CON INPUT YA EN EL POOL
        this.transacciones.addAll(txs)
    }



    private fun llamarMineroRandom() {

        val url: String = urlNodoBc()
        logger.info("REQUEST: <URL $url>")
        val res: ResponseEntity<LinkedHashMap<String, Any>> = restTemplate.postForEntity(url, transacciones)
        logger.info("RESPONSE: <URL $url - RES: $res> ")
        this.transacciones.clear()
    }

    fun buscarResultadosDecision(idOrg: Long, idDec: Long): List<Any>? {
        val url: String = "${urlNodoBc()}/resultados/$idOrg/$idDec"
        logger.info("REQUEST: <URL $url>")
        val res: ResponseEntity<List<Any>> = restTemplate.getForEntity(url)
        logger.info("RESPONSE: <URL $url - RES: $res> ")
        return res.body
    }

    private fun urlNodoBc(): String = "${pares[Random(System.nanoTime()).nextInt(0, pares.size)]}/bc"

}
