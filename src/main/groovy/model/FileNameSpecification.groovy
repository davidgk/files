package model

class FileNameSpecification {
    def initialPart
    String separate
    def endingPart
    def allowFormats
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

    def getElectablePart() {
        return (isElectablePartInit)?initialPart:endingPart;
    }
}
