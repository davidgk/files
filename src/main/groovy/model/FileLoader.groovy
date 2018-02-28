package model

class FileLoader {

    String path
    protected static String Valid ="valid";
    protected static String Invalid ="invalid";
    Map<String,List<FileWrapper>> separateThings = [:]

    FileLoader(path){
        this.path = path
    }

     Map loadFiles(){
        List<File> files = getFilesFromList()
        files.forEach {file ->
            if (analize(file)){
                loadIntoMap(model.FileLoader.Valid, {completeValidFilesAttrites(file)})
            } else {
                loadIntoMap(file, model.FileLoader.Invalid, {createInvalidFileWrapper(file)})
            }
        }
        this.separateThings
    }

    def loadIntoMap(String  key, action ) {
        loadMap( key, action())
    }

    def loadMap(String key, FileWrapper fileWrapper) {
        if (this.separateThings.containsKey(key)){
            this.separateThings.get(key).add(fileWrapper)
        } else {
            this.separateThings.put(key, [fileWrapper])
        }
    }

    List<FileWrapper> createInvalidFileWrapper(File file) {
        return  new InvalidFileWrapper(file)
    }

    def analize(file) {
        String nameFile = file.name;
        String regex = "^(right|left)_\\d+.png\$"
        return  nameFile.matches(regex)
    }


    FileWrapper completeValidFilesAttrites(file){
        return ValidFileWrapper.create(file)
    }

    protected List<File> getFilesFromList() {
        List filesNames = []
        final file  = new File(path)
        if (file.isDirectory()){
            return Arrays.asList(file.listFiles())
        }
        filesNames.add(new File(file.path))
        filesNames
    }
}
