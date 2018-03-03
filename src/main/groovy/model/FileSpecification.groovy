package model

class FileSpecification {
    String path
    AbstractFileNameSpecification nameSpecification
    String divisionPartA
    String divisionPartB

    FileSpecification(path,  nameSpecification){
        this.path = path
        this.nameSpecification =nameSpecification
        this.nameSpecification.setupMainDivision(this)
    }

    String createRegexForAllowed() {
        return nameSpecification.createRegex()
    }

    String getExpressionForElectableDivision() {
        return this.nameSpecification.getExpressionForElectableDivision();
    }

    String getSeparator() {
        return nameSpecification.separate;
    }
}
