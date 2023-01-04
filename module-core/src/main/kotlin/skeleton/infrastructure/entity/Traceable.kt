package skeleton.infrastructure.entity

import java.time.LocalDateTime

interface Traceable {
    var createdAt: LocalDateTime?
    var updatedAt: LocalDateTime?
    var createdBy: String?
    var updatedBy: String?

    companion object {
        object Columns {

            object CreatedAt {
                const val name = "created_at"
            }

            object CreatedBy {
                const val name = "created_by"
                const val length = 100
            }

            object UpdatedAt {
                const val name = "created_at"
            }

            object UpdatedBy {
                const val name = "updated_by"
                const val length = 100
            }
        }
    }
}
