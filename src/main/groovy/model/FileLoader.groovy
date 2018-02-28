package model

class FileLoader {

    String path
    protected static String Valid ="valid";
    protected static String Invalid ="invalid";
    Map<String,List<FileWrapper>> separateThings = [:]
    FileSpecification specification

    FileLoader(FileSpecification specification){
        this.specification = specification
    }

     Map loadFiles(){
        List<File> files = getFilesFromList()
        files.forEach {file ->
            if (analize(file)){
                loadIntoMap(model.FileLoader.Valid, {new ValidFileWrapper(file, specification)})
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
        String regex = specification.createRegexForAllowed()
        return  nameFile.matches(regex)
    }


    protected List<File> getFilesFromList() {
        List filesNames = []
        final file  = new File(specification.path)
        if (file.isDirectory()){
            return Arrays.asList(file.listFiles())
        }
        filesNames.add(new File(file.path))
        filesNames
    }
}
