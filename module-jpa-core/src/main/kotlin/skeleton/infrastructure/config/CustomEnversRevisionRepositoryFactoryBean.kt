package skeleton.infrastructure.config

import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean
import org.springframework.data.repository.history.RevisionRepository
import skeleton.infrastructure.entity.LongTypeRevisionEntity

class CustomEnversRevisionRepositoryFactoryBean<T : RevisionRepository<S, ID, N>, S, ID, N>
constructor(repositoryInterface: Class<T>) : EnversRevisionRepositoryFactoryBean<T, S, ID, N>(repositoryInterface)
        where N : Number, N : Comparable<N> {

    init {
        setRevisionEntityClass(LongTypeRevisionEntity::class.java)
    }
}
