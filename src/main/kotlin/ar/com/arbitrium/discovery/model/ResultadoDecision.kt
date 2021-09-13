package ar.com.arbitrium.discovery.model

class ResultadoDecision(var opcion: Long, var total: Int ) {

    companion object{
        fun desdeLista(salidas: List<Salida>?): List<ResultadoDecision>{
            val o: MutableList<ResultadoDecision> = mutableListOf()
            var rd = ResultadoDecision(0,0)
            for (s in salidas?.sortedBy { it.idDecision }!!){
                if (rd.opcion == s.idOpcion) rd.total++
                else {
                    o.add(rd)
                    rd = ResultadoDecision(s.idOpcion, 1)
                }
            }
            o.add(rd)
            return o
        }
    }
}
