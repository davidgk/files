import spock.lang.Specification

class FileReporterTest extends Specification {

    def "Report"() {
        given:
            def path = "./src/test/resources"
        when:
            String report =  new FileReporter().report(path)
        then:
            report.equals("{pairs:[left:[left_001.png,left_005.png],right:[right_001.png,right_005.png]],failed_pairs:[{error:'size mismatch',left:[left_002.png],right:[right_002.png]},{error:'cannot read',left:[left_006.png],right:[right_006.png]}],orphans:[left:[left_003.png],right:[right_004.png]],ignored:[foo.txt]}")
    }

    def "SeparateAndOrderThem for Correct pair"() {
        given:
            List<File> filesFromList =  new FileReporter().getFilesFromList("./src/test/resources")
        when :
            Map<String,Reporter> map =  new FileReporter().separateAndOrderThem(filesFromList);
        then:
            map.get('ignored').getReport().equals("[foo.txt]")
            map.get('failed_pairs').getReport().equals("[{error:'size mismatch',left:[left_002.png],right:[right_002.png]},{error:'cannot read',left:[left_006.png],right:[right_006.png]}]")
            map.get('orphans').getReport().equals('[left:[left_003.png],right:[right_004.png]]')
            map.get('pairs').getReport().equals("[left:[left_001.png,left_005.png],right:[right_001.png,right_005.png]]" )

    }

    def "GetFilesFromList for a bunch"() {
        given:
            def path = "./src/test/resources"
        when:
            List<File> filesFromList =  new FileReporter().getFilesFromList(path)
        then:
            filesFromList.contains(new File("./src/test/resources/left_001.png"))
            filesFromList.contains(new File("./src/test/resources/left_002.png"))
            filesFromList.contains(new File("./src/test/resources/left_003.png"))
            filesFromList.contains(new File("./src/test/resources/left_005.png"))
            filesFromList.contains(new File("./src/test/resources/right_001.png"))
            filesFromList.contains(new File("./src/test/resources/right_002.png"))
            filesFromList.contains(new File("./src/test/resources/right_004.png"))
            filesFromList.contains(new File("./src/test/resources/right_005.png"))
            filesFromList.contains(new File("./src/test/resources/foo.txt"))
    }

    def "GetFilesFromList for a single"() {
        given:
            def path = "./src/test/resources/left_001.png"
        when:
            List<File> filesFromList =  new FileReporter().getFilesFromList(path)
        then:
            filesFromList.contains(new File("./src/test/resources/left_001.png"))


    }
}
