package demo

import grails.compiler.GrailsCompileStatic
import grails.gorm.MultiTenant
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString
@EqualsAndHashCode
@GrailsCompileStatic
class BookingExtra implements MultiTenant<BookingExtra> {
    Booking booking
    Extra extra

    static constraints = {
        booking nullable: false
        extra nullable: false
    }
}
