package demo

import grails.compiler.GrailsCompileStatic
import grails.gorm.MultiTenant
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString
@EqualsAndHashCode
@GrailsCompileStatic
class BookingExtra implements MultiTenant<BookingExtra> {
    String username
    Booking booking
    Extra extra

    static constraints = {
        username nullable: true
        booking nullable: false
        extra nullable: false
    }

    static mapping = {
        tenantId name: 'username'
    }
}
