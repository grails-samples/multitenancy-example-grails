package demo

import demo.pages.extra.CreateExtraPage
import demo.pages.extra.EditExtraPage
import demo.pages.extra.ExtraListPage
import demo.pages.extra.ShowExtraPage
import geb.spock.GebReportingSpec
import grails.testing.mixin.integration.Integration
import org.grails.datastore.mapping.multitenancy.resolvers.SystemPropertyTenantResolver
import spock.lang.IgnoreIf
import spock.lang.Stepwise

@Integration
@Stepwise
@IgnoreIf({ !System.getProperty('geb.env') })
class ExtraCRUDSpec extends GebReportingSpec {

	def setupSpec() {
		System.setProperty(SystemPropertyTenantResolver.PROPERTY_NAME, 'blue')
	}

	def 'there are no extras'() {
		when:
		ExtraListPage page = to ExtraListPage

		then:
		page.table.numberOfRows() == 0
	}

	def 'add an extra'() {
		given:
		ExtraListPage page = page ExtraListPage

		when:
		page.buttons.create()

		then:
		at CreateExtraPage
	}

	def 'enter the extra details'() {
		given:
		CreateExtraPage page = page CreateExtraPage

		when:
		page.name = 'Breakfast'
		page.save()

		then:
		at ShowExtraPage
	}

	def 'check the entered details for the extra'() {
		given:
		ShowExtraPage page = page ShowExtraPage

		expect:
		page.name == 'Breakfast'
	}

	def 'edit the details'() {
		given:
		ShowExtraPage page = page ShowExtraPage

		when:
		page.buttons.edit()

		then:
		EditExtraPage editPage = at EditExtraPage

		when:
		editPage.name = 'English Breakfast'
		editPage.buttons.update()

		then:
		at ShowExtraPage
	}

	def 'check extra in listing'() {
		when:
		ShowExtraPage page = page ShowExtraPage
		page.nav.extras()

		then:
		at ExtraListPage

		when:
		ExtraListPage listPage = browser.page ExtraListPage

		then:
		listPage.table.numberOfRows() == 1

		when:
		String name = listPage.extraAt(0)

		then:
		name == 'English Breakfast'
	}

	def 'show extra'() {
		given:
		ExtraListPage page = page ExtraListPage

		when:
		page.table.select('English Breakfast')

		then:
		at ShowExtraPage
	}

	def 'delete extra'() {
		given:
		ShowExtraPage page = page ShowExtraPage

		when:
		withConfirm { page.buttons.delete() }

		then:
		at ExtraListPage

		when:
		ExtraListPage listPage = browser.page ExtraListPage

		then:
		listPage.message ==~ /Extra .+ deleted/
		listPage.table.numberOfRows() == 0
	}
}
