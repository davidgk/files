package reporter

class DefaultReporter extends Reporter{

    List<String> fileNames

    static Reporter create(List<String> fileNames){
        DefaultReporter report = new DefaultReporter()
        report.fileNames = fileNames
        report.createReport()
        report
    }

    def createReport() {
        this.report = "["+ createDelimitedByComas(this.fileNames)+"]"
    }
}
