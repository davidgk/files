import model.FileLoader
import model.FileNameSpecification
import model.FileSpecification
import model.FileWrapper
import reporter.Reporter
import spock.lang.Specification

class WrappersContainerTest extends Specification {
    def path = "./src/test/resources"
    def filesFromList =  new FileLoader(new FileSpecification(path, new FileNameSpecification())).loadFiles()

    def complete() {
        given:
            def container = WrappersContainer.create(filesFromList)
        when:
            LinkedHashMap<String, Reporter> map = container.orderThem()
        then:
            map.get('ignored').getReport().equals("[left_001.jpeg,foo.txt,right_001.jpg,left_001.jpg,0001-izquierdo.jpg,0003-derecho.jpg]")
            map.get('failed_pairs').getReport().equals("[{error:'size mismatch',left:[left_002.png],right:[right_002.png]},{error:'cannot read',left:[left_006.png],right:[right_006.png]}]")
            map.get('orphans').getReport().equals('[left:[left_003.png],right:[right_004.png]]')
            map.get('pairs').getReport().equals("[left:[left_001.png,left_005.png],right:[right_001.png,right_005.png]]" )
    }

    def "for Pairs"() {
        given:
            def container = WrappersContainer.create(filesFromList)
        when:
            Reporter result = container.getPairs()
        then:
            result.report.equals("[left:[left_001.png,left_005.png],right:[right_001.png,right_005.png]]")
    }


    def "for orphans"() {
        given:
        def container = WrappersContainer.create(filesFromList)
        when:
        Reporter reporter = container.getOrphans()
        then:
        reporter.getReport().equals('[left:[left_003.png],right:[right_004.png]]')

    }

    def 'for Ignored'() {
        given:
        def container = WrappersContainer.create(filesFromList)
        when:
        Reporter reporter = container.getIgnored()
        then:
        reporter.getReport().equals('[left_001.jpeg,foo.txt,right_001.jpg,left_001.jpg,0001-izquierdo.jpg,0003-derecho.jpg]')

    }

    def "for Failed Pairs"() {
        given:
            def container = WrappersContainer.create(filesFromList)
        when:
            Reporter reporter = container.getFailedPairs()
        then:
            reporter.getReport().equals("[{error:'size mismatch',left:[left_002.png],right:[right_002.png]},{error:'cannot read',left:[left_006.png],right:[right_006.png]}]")
    }

    def "for Failed Pairs cannot Read "() {
        given:
            def container = WrappersContainer.create(filesFromList)
        when:
            List<FileWrapper> result = container.getErrorForCannotRead()
        then:
            result.size() == 1
            result.find{it ->it.code.equals('006')} != null

    }

    def "for Failed Pairs size Mismatch "() {
        given:
            def container = WrappersContainer.create(filesFromList)
        when:
            List<FileWrapper> result = container.getErrorForSizeDifferences()
        then:
            result.size() == 1
            result.find{it ->it.code.equals('002')} != null
    }

}
