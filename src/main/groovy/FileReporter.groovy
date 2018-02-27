class FileReporter {

    String report(String path) {
        def listFiles = getFilesFromList(path)
        Map<String,Reporter> filesOrdered = separateAndOrderThem(listFiles)
        createReport(filesOrdered);
    }

    protected String createReport(Map<String, Reporter> map) {
        StringBuilder message = new StringBuilder("{")
            getMessageFor("pairs", message, map).append(",")
            getMessageFor("failed_pairs", message,map).append(",")
            getMessageFor("orphans", message,map).append(",")
            getMessageFor("ignored", message,map).append("}")
        return message.toString()
    }

    private StringBuilder getMessageFor(String key, StringBuilder builder,Map<String, Reporter> map) {
        builder.append(key).append(":").append(map.get(key).getReport())
        builder
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
