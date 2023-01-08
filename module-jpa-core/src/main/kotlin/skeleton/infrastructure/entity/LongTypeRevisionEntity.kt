package skeleton.infrastructure.entity

import org.hibernate.envers.RevisionEntity
import org.hibernate.envers.RevisionNumber
import org.hibernate.envers.RevisionTimestamp
import skeleton.infrastructure.Converter.epochSecond
import skeleton.infrastructure.entity.LongTypeRevisionEntity.Companion.TABLE_NAME
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@RevisionEntity(LongTypeRevisionEntityListener::class)
@Table(name = TABLE_NAME)
data class LongTypeRevisionEntity(
    @Id @GeneratedValue @RevisionNumber @Column(name = COLUMN_REV)
    var id: Long? = null,
    @RevisionTimestamp @Column(name = COLUMN_REVISION_TIMESTAMP)
    var timestamp: Long,
    @Column(name = COLUMN_USER_ID)
    var userId: String? = null
) {

    @Suppress("UnsafeCallOnNullableType")
    override fun hashCode(): Int {
        return 31 * id!!.toInt() + (timestamp xor (timestamp ushr 32)).toInt() + (userId?.hashCode() ?: 0)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as LongTypeRevisionEntity
        return id == other.id && timestamp == other.timestamp && userId == other.userId
    }

    override fun toString(): String {
        return this::class.simpleName + "(id = $id , timestamp = $timestamp , userId = $userId )"
    }

    companion object {
        const val TABLE_NAME = "rev_info"
        const val COLUMN_REV = "rev"
        const val COLUMN_REVISION_TIMESTAMP = "revtstmp"
        const val COLUMN_USER_ID = "user_id"
        fun of(): LongTypeRevisionEntity = LongTypeRevisionEntity(0L, LocalDateTime.now().epochSecond())
        fun toReadDto(revisionEntity: LongTypeRevisionEntity) =
            LongTypeRevisionEntity(revisionEntity.id, revisionEntity.timestamp, revisionEntity.userId)
    }
}
