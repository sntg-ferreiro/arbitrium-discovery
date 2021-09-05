package ar.com.arbitrium.discovery.repository

import ar.com.arbitrium.discovery.model.NodeHealthError
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface NodeHealthErrorRepository: CrudRepository<NodeHealthError, Long> {
}