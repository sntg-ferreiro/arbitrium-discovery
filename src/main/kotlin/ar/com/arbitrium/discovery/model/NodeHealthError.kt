package ar.com.arbitrium.discovery.model

import javax.persistence.Entity
import javax.persistence.Id

@Entity
class NodeHealthError(@Id val id: Long, val timestamp: String, val status: Int) {
}