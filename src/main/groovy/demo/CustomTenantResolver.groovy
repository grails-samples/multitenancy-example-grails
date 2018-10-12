package demo

import grails.gorm.DetachedCriteria
import grails.plugin.springsecurity.SpringSecurityService
import groovy.transform.CompileStatic
import org.grails.datastore.mapping.multitenancy.AllTenantsResolver
import org.grails.datastore.mapping.multitenancy.exceptions.TenantNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.security.core.userdetails.UserDetails

@CompileStatic
class CustomTenantResolver implements AllTenantsResolver {

    @Lazy
    @Autowired
    SpringSecurityService springSecurityService

    @Override
    Iterable<Serializable> resolveTenantIds() {
        new DetachedCriteria(User).distinct("username").list()
    }

    @Override
    Serializable resolveTenantIdentifier() throws TenantNotFoundException {
        final String tenantId = username()
        if(tenantId) {
            return tenantId
        }
        throw new TenantNotFoundException("Unable to retrieve tenant")
    }

    String username() {
        if(springSecurityService.principal == null) {
            return null
        }
        if (springSecurityService.principal instanceof String) {
            return springSecurityService.principal
        }
        if (springSecurityService.principal instanceof UserDetails) {
            return ((UserDetails)springSecurityService.principal).username
        }

        null
    }
}
