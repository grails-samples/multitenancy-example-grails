package demo

import grails.compiler.GrailsCompileStatic
import grails.gorm.MultiTenant
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString
@EqualsAndHashCode
@GrailsCompileStatic
class BookingRoom implements MultiTenant<BookingRoom> {
    String username
    Booking booking
    Room room
    static constraints = {
        username nullable: true
    }
    static mapping = {
        booking nullable: false
        room nullable: false
        tenantId name: 'username'
    }
}
