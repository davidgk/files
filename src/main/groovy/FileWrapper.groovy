import javax.imageio.ImageIO
import java.awt.image.BufferedImage

class FileWrapper {
    File file
    boolean isElectable;
    boolean isRight;
    boolean isReadable;
    String code
    String kind
    int width
    int height


    static FileWrapper create(File file) {
        FileWrapper wrapper = new FileWrapper();
        wrapper.file = file
        wrapper.analize()
        wrapper
    }

    def analize() {
        String nameFile = this.file.name;
        String regex = "^(right|left)_\\d+.png\$"
        this.isElectable = nameFile.matches(regex)
        if (isElectable) {
            def name = nameFile.split("_")
            this.isRight ="right".equals(name[0])
            def codeAndKind = name[1].split("\\.")
            this.code = codeAndKind[0];
            this.kind = codeAndKind[1];
            checkDimensions()
        }
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
