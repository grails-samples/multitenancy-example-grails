package demo

import grails.compiler.GrailsCompileStatic
import grails.gorm.MultiTenant
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString
@EqualsAndHashCode
@GrailsCompileStatic
class BookingRoom implements MultiTenant<BookingRoom> {
    String hotelname
    Booking booking
    Room room
    static constraints = {
        booking nullable: false
        room nullable: false
        hotelname nullable: true
    }

    static mapping = {
        tenantId name: 'hotelname'
    }
}
