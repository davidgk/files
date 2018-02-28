package model

class FileSpecification {
    String path
    FileNameSpecification nameSpecification
    String divisionPartA
    String divisionPartB

    FileSpecification(path,  nameSpecification){
        this.path = path
        this.nameSpecification =nameSpecification
        this.divisionPartA = nameSpecification.getElectablePart()[0]
        this.divisionPartB = nameSpecification.getElectablePart()[1]
    }

    String createRegexForAllowed() {
        def formats= "(${String.join("|", nameSpecification.allowFormats)})"
        def init =  "(${String.join("|", nameSpecification.initialPart)})"
        def end =  "(${String.join("|", nameSpecification.endingPart)})"
        "^${init}${nameSpecification.separate}${end}.${formats}\$"
    }

    boolean isFirstPartElectable() {
        return this.nameSpecification.isElectablePartInit;
    }

    String getSeparator() {
        return nameSpecification.separate;
    }

    def mainPart() {
        nameSpecification.getElectablePart()
    }
}
