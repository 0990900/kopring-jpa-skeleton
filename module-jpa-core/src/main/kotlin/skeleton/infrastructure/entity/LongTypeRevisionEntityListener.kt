package skeleton.infrastructure.entity

import arrow.core.Option
import arrow.core.getOrElse
import org.hibernate.envers.RevisionListener
import skeleton.infrastructure.RequestContext
import skeleton.infrastructure.SimpleDataHolder

class LongTypeRevisionEntityListener : RevisionListener {

    override fun newRevision(revisionEntity: Any) {
        if (revisionEntity is LongTypeRevisionEntity) {
            RequestContext
                .find<SimpleDataHolder<Option<String>>>(RequestContext.BeanName.USER_DETAILS)
                .flatMap(SimpleDataHolder<Option<String>>::data)
                .getOrElse { "" }
                .also {
                    revisionEntity.userId = it
                }
        }
    }
}
