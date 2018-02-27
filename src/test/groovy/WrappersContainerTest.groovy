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
            map.get('ignored').getReport().equals("[foo.txt]")
            map.get('failed_pairs').getReport().equals("[{error: 'size mismatch', left: left_002.png right: right_002.png}," +
                    "{error: 'cannor read', left: NONE, right: NONE}]")
            map.get('orphans').getReport().equals("[left_003.png]" )
            map.get('pairs').getReport().equals("[{left: left_001.png,left_005.png, right: right_001.png, right_005.png}]" )
    }


    def 'for Ignored'() {
        given:
        def container = WrappersContainer.create(filesFromList)
        when:
        Reporter reporter = container.getIgnored()
        then:
        reporter.getReport().equals('[foo.txt]')

    }

    def "for Failed Pairs"() {
        given:
            def container = WrappersContainer.create(filesFromList)
        when:
            Reporter reporter = container.getFailedPairs()
        then:
            reporter.getReport().equals('[{error: \'size mismatch\', left: left_002.png right: right_002.png},' +
                                       "{error: \'cannor read\', left: NONE, right: NONE}]')")
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
