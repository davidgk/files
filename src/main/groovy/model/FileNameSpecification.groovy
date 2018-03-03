package model

class FileNameSpecification extends AbstractFileNameSpecification{
    def initialPart
    def endingPart
    def isElectablePartInit

    FileNameSpecification(
            def initialPart = ["left", "right"],
            String separate = "_",
            def endingPart = ["\\d+"],
            def allowFormats = ["png"],
            def isElectablePartInit = true
            ){
        this.initialPart = initialPart
        this.separate = separate
        this.endingPart = endingPart
        this.allowFormats = allowFormats
        this.isElectablePartInit = isElectablePartInit
    }

    String createRegex() {
        def formats= "(${String.join("|", allowFormats)})"
        def init =  "(${String.join("|", initialPart)})"
        def end =  "(${String.join("|", endingPart)})"
        "^${init}${separate}${end}.${formats}\$"
    }

    def setupMainDivision(FileSpecification fileSpecification) {
        def part = (isElectablePartInit) ? initialPart : endingPart
        fileSpecification.divisionPartA = part[0]
        fileSpecification.divisionPartB = part[1]
    }

    String getExpressionForElectableDivision(){
        if (isElectablePartInit)
            return "^("+String.join("|", this.initialPart)+")\$"
        else
            return "^("+String.join("|", this.endingPart)+")\$"
    }
}
