package demo

import grails.gorm.multitenancy.CurrentTenant
import grails.gorm.multitenancy.Tenants
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import groovy.time.TimeCategory
import org.grails.datastore.mapping.multitenancy.resolvers.SystemPropertyTenantResolver
import org.grails.orm.hibernate.HibernateDatastore
import spock.lang.Shared
import spock.lang.Specification
import org.hibernate.SessionFactory

@CurrentTenant
@Integration
@Rollback
class BookingDataServiceSpec extends Specification {

    BookingDataService bookingDataService

    HibernateDatastore hibernateDatastore

    @Shared
    Date arrival

    @Shared
    Date departure

    def setupSpec() {
        System.setProperty(SystemPropertyTenantResolver.PROPERTY_NAME, 'blue')

        Date now = new Date()
        use(TimeCategory) {
            arrival = now + 1.day
            departure = now + 3.day
        }
    }

    def cleanupSpec() {
        arrival = null
        departure = null
    }

    void "test get"() {
        when:
        Booking book = new Booking(name: 'Phil', email: 'phil@apple.com', arrival: arrival, departure: departure).save()

        then:
        bookingDataService.get(book.id) != null
    }

    void "test list"() {
        Booking.saveAll(
                new Booking(name: 'Phil', email: 'phil@apple.com', arrival: arrival, departure: departure),
                new Booking(name: 'Steve', email: 'steve@apple.com', arrival: arrival, departure: departure),
                new Booking(name: 'Tim', email: 'tim@apple.com', arrival: arrival, departure: departure),
                new Booking(name: 'Johny', email: 'johny@apple.com', arrival: arrival, departure: departure),
        )

        when:
        List<Booking> bookingList = bookingDataService.list(max: 2, offset: 2)

        then:
        bookingList.size() == 2

        and:
        bookingList[0].name == 'Tim'
        bookingList[0].email == 'tim@apple.com'
        bookingList[0].arrival == arrival
        bookingList[0].departure == departure

        and:
        bookingList[1].name == 'Johny'
        bookingList[1].email == 'johny@apple.com'
        bookingList[1].arrival == arrival
        bookingList[1].departure == departure
    }

    void "test count"() {
        when:
        Booking.saveAll(
                new Booking(name: 'Phil', email: 'phil@apple.com', arrival: arrival, departure: departure),
                new Booking(name: 'Steve', email: 'steve@apple.com', arrival: arrival, departure: departure),
                new Booking(name: 'Tim', email: 'tim@apple.com', arrival: arrival, departure: departure),
                new Booking(name: 'Johny', email: 'johny@apple.com', arrival: arrival, departure: departure),
        )

        then:
        bookingDataService.count() == old(bookingDataService.count()) + 4
    }

    void "test delete"() {
        when:
        Booking booking = new Booking(name: 'Phil', email: 'phil@apple.com', arrival: arrival, departure: departure).save()
        new Booking(name: 'Steve', email: 'steve@apple.com', arrival: arrival, departure: departure).save()
        new Booking(name: 'Tim', email: 'tim@apple.com', arrival: arrival, departure: departure).save()
        new Booking(name: 'Johny', email: 'johny@apple.com', arrival: arrival, departure: departure).save()

        then:
        bookingDataService.count() == old(bookingDataService.count()) + 4

        when:
        bookingDataService.delete(booking.id)
        Serializable tenantId = Tenants.currentId(HibernateDatastore)
        SessionFactory sessionFactory = hibernateDatastore.getDatastoreForConnection(tenantId.toString())
                .getSessionFactory()
        sessionFactory.currentSession.flush()

        then:
        bookingDataService.count() == old(bookingDataService.count()) - 1
    }

    void "test save"() {
        when:

        Booking booking = bookingDataService.save(new Booking(name: 'Phil', email: 'phil@apple.com', arrival: arrival, departure: departure))

        then:
        booking
        booking.id != null
        booking.name == 'Phil'
        booking.email == 'phil@apple.com'
        booking.arrival == arrival
        booking.departure == departure
    }
}
