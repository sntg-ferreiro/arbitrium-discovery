package ar.com.arbitrium.discovery.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Node( val dominio:String, val actuator:String, val puertoP2p: String) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
}