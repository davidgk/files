package model

import spock.lang.Specification

class ValidFileWrapperTest extends Specification {
    
    FileSpecification specification = new FileSpecification("./src/test/resources", new FileNameSpecification())


    def "validate Creator for file 0001-izquierdo.jpg"() {
        given:
        File file = new File("./src/test/resources/0001-izquierdo.jpg");
        when:
        specification = new FileSpecification("./src/test/resources", new FileNameSpecification(["\\d+"],"-",["izquierdo", "derecho"],["jpg"],false))
        ValidFileWrapper wrapper = new ValidFileWrapper(file, specification)
        then:
        specification.divisionPartA == "izquierdo"
        and:
        wrapper.isDivisionA

    }

    def "validate Creator for file 0003-derecho.jpg"() {
        given:
            File file = new File("./src/test/resources/0003-derecho.jpg");
        when:
            specification = new FileSpecification("./src/test/resources", new FileNameSpecification(["\\d+"],"-",["izquierdo", "derecho"],["jpg"],false))
            ValidFileWrapper wrapper = new ValidFileWrapper(file, specification)
        then:
            specification.divisionPartA == "izquierdo"
        and:
            !wrapper.isDivisionA

    }

    def "validate Creator for file right_001.png"() {
        given:
        File file = new File("./src/test/resources/right_001.png");
        when:
        ValidFileWrapper wrapper = new ValidFileWrapper(file, specification)
        then:
        specification.divisionPartA == "left"
        and:
        !wrapper.isDivisionA
    }

    def "validate Creator for file left_001.png"() {
        given:
        File file = new File("./src/test/resources/left_001.png");
        when:
        ValidFileWrapper wrapper = new ValidFileWrapper(file, specification)
        then:
            specification.divisionPartA == "left"
        and:
            wrapper.isDivisionA
    }

    def "Create for one file that is electable right and readable"() {
        given:
            File file = new File("./src/test/resources/right_001.png");
        when:
            ValidFileWrapper wrapper = new ValidFileWrapper(file, specification)
        then:
            wrapper.isReadable
        and:
            !wrapper.isDivisionA
        and:
            wrapper.code == '001'
    }

    def "Create for one file for different formats that is electable and readable"() {
        given:
        File file = new File("./src/test/resources/0001-izquierdo.jpg");
        when:
            specification = new FileSpecification("./src/test/resources", new FileNameSpecification(["\\d+"],"-",["izquierdo", "derecho"],["jpg"],false))
            ValidFileWrapper wrapper = new ValidFileWrapper(file, specification)
        then:
            wrapper.isReadable
        and:
            wrapper.isDivisionA
        and:
            wrapper.code == '0001'
    }

    def "Create for one file that is electable left and readable"() {
        given:
        File file = new File("./src/test/resources/left_001.png");
        when:
        ValidFileWrapper wrapper = new ValidFileWrapper(file, specification)
        then:
            wrapper.isReadable
        and:
            wrapper.isDivisionA
        and:
            wrapper.code == '001'
    }

    def "Create for one file that is electable and not Readable"() {
        given:
        File file = new File("./src/test/resources/left_006.png");
        when:
            ValidFileWrapper wrapper = new ValidFileWrapper(file, specification)
        then:
            !wrapper.isReadable
    }

}
