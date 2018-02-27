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
            this.isReadable = isReadableFile()
            configureDimentions()
        }
    }

    def configureDimentions() {
        BufferedImage bimg = ImageIO.read(this.file)
        this.width          = bimg.getWidth()
        this.height         = bimg.getHeight()
    }

    private isReadableFile() {
        if (!file.exists())
            return false
        if (!file.canRead())
            return false
        try {
            FileReader fileReader = new FileReader(file.getAbsolutePath());
            fileReader.read();
            fileReader.close();
        } catch (Exception e) {
            return false
        }
        return true
    }

    def isDifferent(FileWrapper wrapper){
        return ((this.width != wrapper.width) != (this.height != wrapper.height))
    }
}
