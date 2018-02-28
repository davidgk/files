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

    Boolean getElectableFromName(String[] name, FileSpecification fileSpecification) {
        def mainElectableName = (fileSpecification.isFirstPartElectable())?name[0]:name[1].split("\\.")[0]
        mainElectableName.equals(fileSpecification.divisionPartA)
    }

    String getCodeFromName(String[] name, FileSpecification fileSpecification) {
        def valueToGetCode = (fileSpecification.isFirstPartElectable()) ? name[1].split("\\.")[0] : name[0]
        return valueToGetCode
    }

    ValidFileWrapper(file, specification){
        def name = file.name.split(specification.getSeparator())
        this.isDivisionA =this.getElectableFromName(name, specification)
        def codeAndKind = file.name.split("\\.")
        this.kind = codeAndKind[1]
        this.code= this.getCodeFromName(name, specification)
        this.file = file
        checkDimensions()
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
