package demo

import grails.gorm.multitenancy.CurrentTenant
import grails.gorm.multitenancy.Tenants
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import org.grails.datastore.mapping.multitenancy.resolvers.SystemPropertyTenantResolver
import org.grails.orm.hibernate.HibernateDatastore
import spock.lang.Specification
import org.hibernate.SessionFactory

@CurrentTenant
@Integration
@Rollback
class ExtraDataServiceSpec extends Specification {

    ExtraDataService extraDataService

    HibernateDatastore hibernateDatastore

    def setupSpec() {
        System.setProperty(SystemPropertyTenantResolver.PROPERTY_NAME, 'blue')
    }


    void "test get"() {
        when:
        Extra extra = new Extra(name: 'Breakfast').save()

        then:
        extraDataService.get(extra.id) != null
    }

    void "test list"() {
        given:
        new Extra(name: 'Breakfast').save()
        new Extra(name: 'Crib').save()
        new Extra(name: 'Champagne').save()
        new Extra(name: 'Late Departure').save()

        when:
        List<Extra> extraList = extraDataService.list(max: 2, offset: 2)

        then:
        extraList.size() == 2
        extraList[0].name == 'Champagne'
        extraList[1].name == 'Late Departure'
    }

    void "test count"() {
        when:
        new Extra(name: 'Breakfast').save()
        new Extra(name: 'Crib').save()
        new Extra(name: 'Champagne').save()
        new Extra(name: 'Late Departure').save()

        then:
        extraDataService.count() == old(extraDataService.count()) + 4
    }

    void "test delete"() {
        when:
        Extra extra = new Extra(name: 'Breakfast').save()
        new Extra(name: 'Crib').save()
        new Extra(name: 'Champagne').save()
        new Extra(name: 'Late Departure').save()

        then:
        extraDataService.count() == old(extraDataService.count()) + 4

        when:
        extraDataService.delete(extra.id)
        Serializable tenantId = Tenants.currentId(HibernateDatastore)
        SessionFactory sessionFactory = hibernateDatastore.getDatastoreForConnection(tenantId.toString())
                .getSessionFactory()
        sessionFactory.currentSession.flush()

        then:
        extraDataService.count() == old(extraDataService.count()) - 1
    }

    void "test save"() {
        when:
        Extra extra = extraDataService.save(new Extra(name: 'Breakfast'))

        then:
        extra
        extra.id != null
        extra.name == 'Breakfast'
    }
}
