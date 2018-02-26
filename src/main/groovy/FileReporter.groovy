class FileReporter {

    String report(String path) {
        def listFiles = getFilesFromList(path)
        Map<String,Reporter> filesOrdered = separateAndOrderThem(listFiles)
        createReport(filesOrdered);
    }

    String createReport(Map<String, List<String>> stringListMap) {
        null
    }

    Map<String,Reporter> separateAndOrderThem(List<File> files) {
        WrappersContainer container = WrappersContainer.create(files.collect {FileWrapper.create(it)})
        Map<String,List<String>> separateAndOrderThem  = container.separateThem().orderThem()
        separateAndOrderThem
    }


    List<File> getFilesFromList(String path) {
        List filesNames = []
        final file  = new File(path)
        if (file.isDirectory()){
            return Arrays.asList(file.listFiles())
        }
        filesNames.add(new File(file.path))
        filesNames
    }
}
