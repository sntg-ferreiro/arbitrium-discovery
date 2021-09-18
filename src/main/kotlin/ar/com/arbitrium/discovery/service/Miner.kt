package ar.com.arbitrium.discovery.service

import ar.com.arbitrium.discovery.model.ResultadoDecision
import ar.com.arbitrium.discovery.model.Salida
import ar.com.arbitrium.discovery.model.Transaccion
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
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

    fun checkSalud(): LinkedHashMap<String, String> {
        var resultado: LinkedHashMap<String, String> = linkedMapOf()
        pares.forEach {
            var res: ResponseEntity<String>? = null
            try {
                res = restTemplate.exchange("$it/actuator/health", HttpMethod.GET)
            }catch (e: Exception){
                logger.info("EXCEPTION! [CLASS: ${e.javaClass} - MSG: ${e.localizedMessage}]")
                resultado[it] = e.localizedMessage
            }

            if (res != null) {
                logger.info("Nodo: $it - Salud: ${res.body}")
                res.body?.let { status -> resultado[it] = status }
            }
        }
        return resultado
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
        var res: Any? = llamarNodo("", HttpMethod.POST, HttpEntity(transacciones))
        this.transacciones.clear()
    }

    fun buscarResultadosDecision(idOrg: Long, idDec: Long): List<ResultadoDecision> {
        val endpoint = "/resultados/$idOrg/$idDec"
        val o: List<Salida>? =  llamarNodo(endpoint, HttpMethod.GET, HttpEntity.EMPTY as HttpEntity<Any>)
        return ResultadoDecision.desdeLista(o)
    }

    private inline fun <reified T> llamarNodo(endpoint: String, method: HttpMethod, entity: HttpEntity<Any>): T? {
        val url = "${urlNodoBc()}$endpoint"
        logger.info("REQUEST: <URL $url>")
        return try{
            val res: ResponseEntity<T> = restTemplate.exchange(url, method,entity)
            logger.info("RESPONSE: <URL $url - RES: $res> ")
            res.body
        }catch (e: Exception){
            //retry
            val url2: String = "${urlNodoBc(0)}$endpoint"
            logger.info("REQUEST: <URL $url2>")
            val res: ResponseEntity<T> = restTemplate.exchange(url2, method,entity)
            logger.info("RESPONSE: <URL $url2 - RES: $res> ")
            res.body
        }

    }

    private fun urlNodoBc(): String = "${pares[Random(System.nanoTime()).nextInt(0, pares.size)]}/bc"
    private fun urlNodoBc(i: Int): String = "${pares[i]}/bc"

}
