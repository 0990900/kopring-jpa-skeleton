package skeleton.infrastructure

import arrow.core.Option
import arrow.core.toOption
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.context.request.RequestContextHolder

object RequestContext {
    inline fun <reified T> find(attributeName: String): Option<T> {
        return (
            RequestContextHolder.getRequestAttributes()
                ?.getAttribute(attributeName, RequestAttributes.SCOPE_REQUEST) as? T
            ).toOption()
    }

    object BeanName {
        private const val SCOPED_TARGET = "scopedTarget"
        const val USER_DETAILS = "$SCOPED_TARGET.userDetails"
    }
}
