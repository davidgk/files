class FileWrapper {
    File file
    boolean isElectable;
    boolean isRight;
    boolean isReadable;
    String code


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
            this.code = name[1].split("\\.")[0];
        }
        this.isReadable = isReadableFile()
        this.isElectable &=this.isReadable
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
        def internal = file.length()
        def external = wrapper.file.length()
        return (internal != external)
    }
}
