import spock.lang.Specification

class FileWrapperTest extends Specification {

    def "Create for one file that is electable right and readable"() {
        given:
            File file = new File("./src/test/resources/right_001.png");
        when:
            FileWrapper wrapper = FileWrapper.create(file)
        then:
            wrapper.isElectable
        and:
            wrapper.isReadable
        and:
            wrapper.isRight
        and:
            wrapper.code == '001'
    }

    def "Create for one file that is electable left and readable"() {
        given:
        File file = new File("./src/test/resources/left_001.png");
        when:
        FileWrapper wrapper = FileWrapper.create(file)
        then:
            wrapper.isElectable
        and:
            wrapper.isReadable
        and:
            !wrapper.isRight
        and:
            wrapper.code == '001'
    }

    def "Create for one file that is !electable"() {
        given:
            File file = new File("./src/test/resources/foo.txt");
        when:
            FileWrapper wrapper = FileWrapper.create(file)
        then:
            !wrapper.isElectable
    }

    def "Create for one file that is electable and not Readable"() {
        given:
        File file = new File("./src/test/resources/left_006.png");
        when:
            FileWrapper wrapper = FileWrapper.create(file)
        then:
            wrapper.isElectable
        and:
            !wrapper.isReadable
    }

    def "Create for one file that is electable with name like 001_left.txt"() {
        given:
        def filename = "001_left.txt";
        expect:
        testForFilesNotElectable("./src/test/resources/$filename")
    }

    def "Create for one file that is electable with name like left_001.txt 01"() {
        given:
        def filename = "left_001.txt";
        expect:
        testForFilesNotElectable("./src/test/resources/$filename");

    }

    def "Create for one file that is electable with name like left001.png 02"() {
        given:
        def filename = "left001.png";
        expect:
        testForFilesNotElectable("./src/test/resources/$filename")
    }

    def "Create for one file that is electable with name like ASDleft_001.png 03"() {
        given:
        def filename = "ASDleft_001.png"
        expect:
        testForFilesNotElectable("./src/test/resources/$filename")
    }


    def testForFilesNotElectable(filename) {
        File file = new File(filename);
        FileWrapper wrapper = FileWrapper.create(file)
        !wrapper.isElectable
    }
}
