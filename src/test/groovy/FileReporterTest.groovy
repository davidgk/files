import spock.lang.Specification

class FileReporterTest extends Specification {

    def "Report"() {
        given:
            def path = "./src/test/resources"
        when:
            String report =  new FileReporter().report(path)
        then:
            report.equals("{others:[foo.txt],corrupts:[],alone:[left:[003], right:[004]],different:[002],correct:[001,005]}")
    }

    def "CreateReport"() {

    }

    def "SeparateAndOrderThem for Correct pair"() {
        given:
            List<File> filesFromList =  new FileReporter().getFilesFromList("./src/test/resources")
        when :
            Map<String,Reporter> map =  new FileReporter().separateAndOrderThem(filesFromList);
        then:
            map.get('others').getReport().equals('[foo.txt]')
            map.get('corrupts').getReport().equals('[]')
            map.get('alone').getReport().equals('[left:[003], right:[004]]')
            map.get('different').getReport().equals('[002]')
            map.get('correct').getReport().equals('[001,005]')

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
