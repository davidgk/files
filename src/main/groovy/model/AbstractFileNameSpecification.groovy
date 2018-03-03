package model

abstract class AbstractFileNameSpecification {
    String separate
    def allowFormats

    abstract String getExpressionForElectableDivision

    abstract String createRegex()

    abstract def setupMainDivision(FileSpecification fileSpecification)
}
