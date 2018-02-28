package reporter
import model.FileWrapper

class CommonLeftAndRightReporter extends ReporterForSeparate{

    static Reporter create(List<FileWrapper> lefts, List<FileWrapper> rights){
        CommonLeftAndRightReporter report = new CommonLeftAndRightReporter()
        report.separateThings.put(CommonLeftAndRightReporter.LEFT,lefts)
        report.separateThings.put(CommonLeftAndRightReporter.RIGHT,rights)
        report.createReport()
        report
    }

    static Reporter createFromOnlyOneList(List<FileWrapper> onceList){
        CommonLeftAndRightReporter report = new CommonLeftAndRightReporter()
        report.separateThings.put(CommonLeftAndRightReporter.LEFT,onceList)
        report.separateThings.put(CommonLeftAndRightReporter.RIGHT,onceList)
        report.createReport()
        report
    }

    def createReport() {
        this.report ="["+ createDelimitedByComas(getReportForBothHands())+"]"
    }


}
