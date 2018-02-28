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
                loadIntoMap(model.FileLoader.Valid, {ValidFileWrapper.create(file)})
            } else {
                loadIntoMap(model.FileLoader.Invalid, {new InvalidFileWrapper(file)})
            }
        }
        this.separateThings
    }

    protected def loadIntoMap(String  key, action ) {
        if (this.separateThings.containsKey(key)){
            this.separateThings.get(key).add(action())
        } else {
            this.separateThings.put(key, [action()])
        }
    }

    private boolean analize(file) {
        String nameFile = file.name;
        String regex = "^(right|left)_\\d+.png\$"
        return  nameFile.matches(regex)
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
