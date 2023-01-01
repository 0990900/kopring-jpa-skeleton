package skeleton.infrastructure

import org.hibernate.Hibernate
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import skeleton.infrastructure.Traceable.Companion.Columns
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass
import javax.persistence.Transient

@Suppress("UnnecessaryAbstractClass")
@EntityListeners(AuditingEntityListener::class)
@MappedSuperclass
abstract class BaseEntity : Identifiable, Traceable {
    @field:[Id GeneratedValue(strategy = GenerationType.IDENTITY)]
    override var id: Long? = null

    @field:[Column(name = Columns.CreatedAt.name) CreatedDate]
    @get:[IdentityColumn]
    override var createdAt: LocalDateTime? = null

    @get:[IdentityColumn]
    @field:[Column(name = Columns.CreatedBy.name, length = Columns.CreatedBy.length) CreatedBy]
    override var createdBy: String? = null

    @get:[IdentityColumn]
    @field:[Column(name = Columns.UpdatedAt.name) LastModifiedDate]
    override var updatedAt: LocalDateTime? = null

    @get:[IdentityColumn]
    @field:[Column(name = Columns.UpdatedBy.name, length = Columns.UpdatedBy.length) CreatedBy]
    override var updatedBy: String? = null

    @get:Transient
    override val isNew: Boolean
        get() = (id == null)

    override fun hashCode(): Int = this.id?.hashCode() ?: EntityHelper.transientHashCode(this)

    override fun equals(other: Any?): Boolean = other?.let {
        if (Hibernate.getClass(this) != Hibernate.getClass(it)) {
            false
        } else {
            when (other) {
                is BaseEntity -> {
                    if (isNew && other.isNew) EntityHelper.transientEquals(this, other)
                    else id == other.id
                }

                else -> false
            }
        }
    } ?: false
}
