import model.FileLoader
import model.FileSpecification
import model.FileWrapper
import reporter.Reporter

class MainFileReporter {

    String report(FileSpecification specification) {
        def mapFiles = new FileLoader(specification).loadFiles()
        Map<String,Reporter> filesOrdered = separateAndOrderThem(mapFiles )
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

    protected Map<String,Reporter> separateAndOrderThem(Map<String, List<FileWrapper>> files) {
        WrappersContainer container = WrappersContainer.create(files)
        return container.orderThem()
    }


}
