package ar.com.arbitrium.discovery.repository

import ar.com.arbitrium.discovery.model.Node
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface NodeRepository: CrudRepository<Node, Long>