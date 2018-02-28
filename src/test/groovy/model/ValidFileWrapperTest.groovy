package model

import spock.lang.Specification

class ValidFileWrapperTest extends Specification {

    def "Create for one file that is electable right and readable"() {
        given:
            File file = new File("./src/test/resources/right_001.png");
        when:
            ValidFileWrapper wrapper = ValidFileWrapper.create(file)
        then:
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
        ValidFileWrapper wrapper = ValidFileWrapper.create(file)
        then:
            wrapper.isReadable
        and:
            !wrapper.isRight
        and:
            wrapper.code == '001'
    }

    def "Create for one file that is electable and not Readable"() {
        given:
        File file = new File("./src/test/resources/left_006.png");
        when:
            ValidFileWrapper wrapper = ValidFileWrapper.create(file)
        then:
            !wrapper.isReadable
    }

}
