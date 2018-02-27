class FileReporter {

    String report(String path) {
        def listFiles = getFilesFromList(path)
        Map<String,Reporter> filesOrdered = separateAndOrderThem(listFiles)
        createReport(filesOrdered);
    }

    protected String createReport(Map<String, Reporter> stringListMap) {
        def messageFinal = []
        stringListMap.forEach{ key, value ->
            StringBuilder message = new StringBuilder(key)
            message.append(":")
            message.append(value.getReport())
            messageFinal.add(message.toString())
        }
        return "{"+String.join(",", messageFinal)+"}"
    }

    protected Map<String,Reporter> separateAndOrderThem(List<File> files) {
        WrappersContainer container = WrappersContainer.create(files.collect {FileWrapper.create(it)})
        return container.orderThem()
    }


    protected List<File> getFilesFromList(String path) {
        List filesNames = []
        final file  = new File(path)
        if (file.isDirectory()){
            return Arrays.asList(file.listFiles())
        }
        filesNames.add(new File(file.path))
        filesNames
    }
}
