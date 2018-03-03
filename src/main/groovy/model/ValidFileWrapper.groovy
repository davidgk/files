package model

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

class ValidFileWrapper extends FileWrapper{
    boolean isDivisionA;
    boolean isReadable;
    String code
    String kind
    int width
    int height

    ValidFileWrapper(file, specification){
        def name = file.name.split(specification.getSeparator())
        this.isDivisionA =this.getElectableFromName(name, specification)
        def codeAndKind = file.name.split("\\.")
        this.kind = codeAndKind[1]
        this.code= this.getCodeFromName(name, specification)
        this.file = file
        checkDimensions()
    }


    Boolean getElectableFromName(String[] name, FileSpecification fileSpecification) {
        def valToEvaluateA = name[0]
        def valToEvaluateB = name[1].split("\\.")[0]
        def expression = fileSpecification.getExpressionForElectableDivision()
        if (valToEvaluateA.matches(expression))
            return valToEvaluateA.equals(fileSpecification.divisionPartA)
        else if (valToEvaluateB.matches(expression)) {
            return valToEvaluateB.equals(fileSpecification.divisionPartA)
        }
        return false
    }

    String getCodeFromName(String[] name, FileSpecification fileSpecification) {
        def valToEvaluateA = name[1].split("\\.")[0]
        def valToEvaluateB = name[0]
        def expression = fileSpecification.getExpressionForElectableDivision()
        if (valToEvaluateA.matches(expression))
            return valToEvaluateB
        else if (valToEvaluateB.matches(expression)) {
            return valToEvaluateA
        }
        return null
    }



    def checkDimensions() {
        try {
            BufferedImage bimg = ImageIO.read(this.file)
            this.width          = bimg.getWidth()
            this.height         = bimg.getHeight()
            this.isReadable = true;
        } catch (e) {
            this.isReadable = false;
        }
    }

    def isDifferent(FileWrapper wrapper){
        return ((this.width != wrapper.width) != (this.height != wrapper.height))
    }
}
