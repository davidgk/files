import model.FileLoader
import reporter.Reporter
import spock.lang.Specification

class FileReporterTest extends Specification {

    def "Report"() {
        given:
            def path = "./src/test/resources"
        when:
            String report =  new MainFileReporter().report(path)
        then:
            report.equals("{pairs:[left:[left_001.png,left_005.png],right:[right_001.png,right_005.png]],failed_pairs:[{error:'size mismatch',left:[left_002.png],right:[right_002.png]},{error:'cannot read',left:[left_006.png],right:[right_006.png]}],orphans:[left:[left_003.png],right:[right_004.png]],ignored:[foo.txt]}")
    }

    def "SeparateAndOrderThem for Correct pair"() {
        given:
            def filesFromList = new FileLoader("./src/test/resources").loadFiles()
        when :
            Map<String,Reporter> map =  new MainFileReporter().separateAndOrderThem(filesFromList)
        then:
            map.get('ignored').getReport().equals("[foo.txt]")
            map.get('failed_pairs').getReport().equals("[{error:'size mismatch',left:[left_002.png],right:[right_002.png]},{error:'cannot read',left:[left_006.png],right:[right_006.png]}]")
            map.get('orphans').getReport().equals('[left:[left_003.png],right:[right_004.png]]')
            map.get('pairs').getReport().equals("[left:[left_001.png,left_005.png],right:[right_001.png,right_005.png]]" )

    }

}
