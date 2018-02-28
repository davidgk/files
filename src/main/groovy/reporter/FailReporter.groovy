package reporter
import model.FileWrapper

class FailReporter extends ReporterForSeparate{

    String error

    static Reporter create(String error, List<FileWrapper> errors){
        FailReporter report = new FailReporter()
        report.separateThings.put(CommonLeftAndRightReporter.LEFT,errors)
        report.separateThings.put(CommonLeftAndRightReporter.RIGHT,errors)
        report.error = error
        report.createReport()
        report
    }

    def createReport() {
        StringBuilder builder = new StringBuilder("{error:")
            builder.append("'").append(error).append("'").append(",")
            .append(createDelimitedByComas(getReportForBothHands()))
            .append("}")
        this.report =builder.toString()
    }

}
