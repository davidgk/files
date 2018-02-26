import spock.lang.Specification

class WrappersContainerTest extends Specification {
    def path = "./src/test/resources"
    List<FileWrapper>  filesFromList =  new FileReporter().getFilesFromList(path).collect {it ->FileWrapper.create(it)}

    def complete() {
        given:
            def container = WrappersContainer.create(filesFromList)
        when:
            LinkedHashMap<String, Reporter> map = container.orderThem()
        then:
            map.get('others').getReport().equals('[foo.txt]')
            map.get('corrupts').getReport().equals('[]')
            map.get('alone').getReport().equals('[left:[003], right:[004]]')
            map.get('different').getReport().equals('[002]')
            map.get('correct').getReport().equals('[001,005]')
    }

    def "for corrupts"() {
        given:
            def container = WrappersContainer.create(filesFromList)
        when:
            Reporter reporter = container.getCorruptNames()
        then:
            reporter.getReport().equals('[]')
    }

    def "for others"() {
        given:
            def container = WrappersContainer.create(filesFromList)
        when:
            Reporter reporter = container.getOthers()
        then:
            reporter.getReport().equals('[foo.txt]')

    }

    def "for alones"() {
        given:
            def container = WrappersContainer.create(filesFromList)
            container.separateElectablesLeftFromRights()
        when:
            Reporter reporter = container.getAlones()
        then:
            reporter.getReport().equals('[left:[003], right:[004]]')

    }

    def "for different"() {
        given:
            def container = WrappersContainer.create(filesFromList)
            container.separateElectablesLeftFromRights()
        when:
            Reporter reporter = container.getDiferents()
        then:
            reporter.getReport().equals('[002]')

    }

    def "for Correct"() {
        given:
        def container = WrappersContainer.create(filesFromList)
        container.separateElectablesLeftFromRights()
        when:
        Reporter reporter = container.getCorrects()
        then:
        reporter.getReport().equals('[001,005]')

    }

}