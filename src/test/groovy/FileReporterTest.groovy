import spock.lang.Specification

class FileReporterTest extends Specification {

    def "Report"() {
        given:
            def path = "./src/test/resources"
        when:
            String report =  new FileReporter().report(path)
        then:
            report.equals("{correct:['001'], different:['002'], alone:[left:['003'], right:['004']], others:['foo.txt'], corrupt:['005']}")
    }

    def "CreateReport"() {

    }

    def "SeparateAndOrderThem for Correct pair"() {
        given:
            List<File> filesFromList = [new File("./src/test/resources/left_001.png"), new File("./src/test/resources/right_002.png")]
        when :
            Map<String,List<String>> map =  new FileReporter().separateAndOrderThem(filesFromList);
        then:
            map.get('correct').size() == 1
            map.get('correct')[0].equals('001')
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
