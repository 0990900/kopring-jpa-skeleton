package skeleton.infrastructure.entity

interface Identifiable {
    var id: Long?
    val isNew: Boolean
}
