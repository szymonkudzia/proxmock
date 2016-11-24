package com.sk.app.proxmock

import com.sk.app.proxmock.application.domain.Config
import com.sk.app.proxmock.toolset.serialization.Yaml
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.ComponentScan.Filter
import org.springframework.context.annotation._

@Configuration
@EnableAutoConfiguration
@ComponentScan(
  basePackageClasses = Array(classOf[ProxmockTestConfiguration]),
  includeFilters = Array(
    // treat test classes as spring configuration providers
    new Filter(`type` = FilterType.ASSIGNABLE_TYPE, value = Array(classOf[BaseIntegrationTest]))
  ))
class ProxmockTestConfiguration {
  @Bean
  @Primary
  def config(@Autowired endpoints: Array[String]) = {
    val configYaml =
    s"""
      |name: test instance
      |port: 60000
      |endpoints:
      |${endpoints.mkString("\n")}
    """.stripMargin

    Yaml.parse(configYaml, classOf[Config])
  }
}