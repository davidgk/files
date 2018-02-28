import model.FileLoader
import model.FileNameSpecification
import model.FileSpecification
import reporter.Reporter
import spock.lang.Specification

class FileReporterTest extends Specification {

    def "Report"() {
        given:
            def path = "./src/test/resources"
        when:
            String report =  new MainFileReporter().report(new FileSpecification(path, new FileNameSpecification()))
        then:
            report.equals("{pairs:[left:[left_001.png,left_005.png],right:[right_001.png,right_005.png]],failed_pairs:[{error:'size mismatch',left:[left_002.png],right:[right_002.png]},{error:'cannot read',left:[left_006.png],right:[right_006.png]}],orphans:[left:[left_003.png],right:[right_004.png]],ignored:[left_001.jpeg,foo.txt,right_001.jpg,left_001.jpg,0001-izquierdo.jpg,0003-derecho.jpg]}")
    }

    def "SeparateAndOrderThem for Correct pair"() {
        given:
            def path = "./src/test/resources"
            def specification = new FileSpecification(path, new FileNameSpecification())
            def filesFromList =  new FileLoader(specification).loadFiles()
        when :
            Map<String,Reporter> map =  new MainFileReporter().separateAndOrderThem(filesFromList, specification)
        then:
            map.get('ignored').getReport().equals("[left_001.jpeg,foo.txt,right_001.jpg,left_001.jpg,0001-izquierdo.jpg,0003-derecho.jpg]")
            map.get('failed_pairs').getReport().equals("[{error:'size mismatch',left:[left_002.png],right:[right_002.png]},{error:'cannot read',left:[left_006.png],right:[right_006.png]}]")
            map.get('orphans').getReport().equals('[left:[left_003.png],right:[right_004.png]]')
            map.get('pairs').getReport().equals("[left:[left_001.png,left_005.png],right:[right_001.png,right_005.png]]" )

    }

}
