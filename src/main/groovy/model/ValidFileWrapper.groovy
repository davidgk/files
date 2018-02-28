package model

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

class ValidFileWrapper extends FileWrapper{
    boolean isRight;
    boolean isReadable;
    String code
    String kind
    int width
    int height

    static FileWrapper create(File file) {
        def name = file.name.split("_")
        def isRight ="right".equals(name[0])
        def codeAndKind = name[1].split("\\.")
        def code = codeAndKind[0];
        def kind = codeAndKind[1];
        return new ValidFileWrapper(isRight,code, kind, file)
    }
    ValidFileWrapper(isRight,code, kind, file){
        this.isRight = isRight
        this.code = code
        this.kind = kind
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
