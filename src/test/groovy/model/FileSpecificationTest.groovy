package model

import spock.lang.Specification

class FileSpecificationTest extends Specification {


    def "CreateRegexForAllowed"() {
        given:
            Object path = "./src/test/resources/left_001.png"
            List electableParts = ["left", "right"]
            FileNameSpecification nameSpecification  = new FileNameSpecification(
                    electableParts,"_",["\\d+"],["png"],true)
            FileSpecification sut = new FileSpecification(path,  nameSpecification)
        when:
            def regexExpected = sut.createRegexForAllowed()
        then:
            regexExpected.equals("^(left|right)_(\\d+).(png)\$")
    }
}
