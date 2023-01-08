package skeleton.infrastructure.config

import com.zaxxer.hikari.HikariDataSource
import mu.KotlinLogging
import org.h2.tools.Server
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.web.config.EnableSpringDataWebSupport
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.transaction.support.TransactionSynchronizationManager
import java.sql.SQLException
import javax.sql.DataSource

@Configuration
@EnableSpringDataWebSupport
@EnableJpaAuditing(auditorAwareRef = "auditor")
@EnableConfigurationProperties(DataSourcePropertiesGroup::class)
@Suppress("UnnecessaryAbstractClass")
/**
 * plz, add a configuration.
 * ```
 * @EnableJpaRepositories(
 *     basePackages = ["your-repo-package"],
 *     repositoryFactoryBeanClass = CustomEnversRevisionRepositoryFactoryBean::class
 * )
 * ```
 */
abstract class AutoDatabaseConfig(
    @Value("\${spring.profiles.active:}") val profile: String
) {

    @Bean
    @ConditionalOnBean(DataSourcePropertiesGroup::class)
    fun dataSource(dataSourcePropertiesGroup: DataSourcePropertiesGroup): DataSource {
        val dataSourcePropertiesPair: DataSourcePropertiesPair = dataSourcePropertiesGroup.getDataSourceProperties()

        require(dataSourcePropertiesPair.first.dataSourceType == DataSourcePropertiesGroup.DataSourceType.MASTER)
        require(dataSourcePropertiesPair.second.dataSourceType == DataSourcePropertiesGroup.DataSourceType.SLAVE)

        val masterDataSource = HikariDataSource(dataSourcePropertiesPair.first.dataSourceProperties)
        val dataSourceMap = mapOf(
            dataSourcePropertiesPair.first.dataSourceType as Any to
                masterDataSource as Any,
            dataSourcePropertiesPair.second.dataSourceType as Any to
                HikariDataSource(dataSourcePropertiesPair.second.dataSourceProperties) as Any
        )

        val replicationRoutingDataSource: AbstractRoutingDataSource = object : AbstractRoutingDataSource() {
            override fun determineCurrentLookupKey(): Any {
                return if (TransactionSynchronizationManager.isCurrentTransactionReadOnly()) {
                    log.debug { "Current DataSource : SLAVE" }
                    DataSourcePropertiesGroup.DataSourceType.SLAVE
                } else {
                    log.debug { "Current DataSource : MASTER" }
                    DataSourcePropertiesGroup.DataSourceType.MASTER
                }
            }
        }
        replicationRoutingDataSource.setTargetDataSources(dataSourceMap)
        replicationRoutingDataSource.setDefaultTargetDataSource(masterDataSource)
        replicationRoutingDataSource.afterPropertiesSet()
        return LazyConnectionDataSourceProxy(replicationRoutingDataSource)
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    @Throws(SQLException::class)
    fun h2TcpServer(): ServerDelegator = if (profile.contains("h2")) {
        object : ServerDelegator {
            private val server = Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092")
            override fun start() {
                server.start()
                log.info { "H2 Console started - http://localhost:8080/h2-console" }
            }

            override fun stop() {
                server.stop()
            }
        }
    } else {
        ServerDelegator.empty
    }

    companion object {
        private val log = KotlinLogging.logger { }
    }
}
